/*
 * JBoss, Home of Professional Open Source. Copyright 2008, Red Hat Middleware LLC, and individual contributors as
 * indicated by the @author tags. See the copyright.txt file in the distribution for a full listing of individual
 * contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.picketlink.test.integration.sts;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SimplePrincipal;
import org.junit.Assert;
import org.junit.Test;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.wstrust.SamlCredential;
import org.picketlink.identity.federation.core.wstrust.WSTrustConstants;
import org.picketlink.identity.federation.core.wstrust.WSTrustUtil;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.picketlink.identity.federation.core.wstrust.wrappers.RequestSecurityToken;
import org.w3c.dom.Element;

/**
 * <p>
 * This class tests the invalidation of security cache entries that contain expired tokens. This mechanism is enabled by
 * setting the {@code cache.invalidation} property of the {@code SAML2STSLoginModule} to {@code true} and causes the
 * security cache of the JBoss Application Server to remove (logout) users whose SAML assertions have expired.
 * </p>
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 * @since Jun 8, 2010
 */
public class CacheInvalidationUnitTestCase
{
   /**
    * <p>
    * This test checks the invalidation of expired cache entries by requesting a short-lived assertion to the STS
    * and then using this assertion to authenticate to the {@code JaasSecurityManagerService} MBean. The test checks
    * if the cache contains the entry right after authentication takes place and then sleeps till the assertion
    * expires. After that, the test checks the cache again to verify if the entry has been removed.
    * </p>
    * 
    * @throws Exception if an error occurs while running the test.
    */
   @Test
   public void testCacheInvalidation() throws Exception
   {
      // initial context properties that specify how to connect to the JBoss JNDI server.
      Properties props = new Properties();
      props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
      props.put("java.naming.provider.url", "localhost:1099");

      // lookup the RMIAdaptor instance in JNDI.
      InitialContext ic = new InitialContext(props);
      RMIAdaptor server = (RMIAdaptor) ic.lookup("jmx/invoker/RMIAdaptor");
      Assert.assertNotNull("RMIAdaptor is null, lookup failed", server);
      
      // invoke the token service to obtain a short-lived (10s) assertion.
      WSTrustClient client = new WSTrustClient("PicketLinkSTS", "PicketLinkSTSPort",
            "http://localhost:8080/picketlink-sts/PicketLinkSTS", new SecurityInfo("admin", "admin"));
      RequestSecurityToken request = new RequestSecurityToken();
      request.setRequestType(URI.create(WSTrustConstants.ISSUE_REQUEST));
      request.setTokenType(URI.create(SAMLUtil.SAML2_TOKEN_TYPE));
      request.setLifetime(WSTrustUtil.createDefaultLifetime(10000));
      Element assertionElement = client.issueToken(request);
      Assert.assertNotNull("SAML assertion is null, token request failed", assertionElement);

      // invoke the JaasSecurityManagerService MBean to authenticate the client using the assertion.
      ObjectName name = new ObjectName("jboss.security:service=JaasSecurityManager");
      String[] methodSignature = {"java.lang.String", "java.security.Principal", "java.lang.Object"};
      Object[] methodParams = {"cache-test", new SimplePrincipal("admin"), new SamlCredential(assertionElement)};
      Object result = server.invoke(name, "isValid", methodParams, methodSignature);
      Assert.assertTrue("isValid returned an invalid result object", result instanceof Boolean);
      Assert.assertTrue("Authentication failed", (Boolean) result);
      
      // check if the cache contains the authenticated principal.
      methodSignature = new String[]{"java.lang.String"};
      methodParams = new Object[]{"cache-test"};
      result = server.invoke(name, "getAuthenticationCachePrincipals", methodParams, methodSignature);
      Assert.assertTrue("getAuthenticationCachePrincipals returned an invalid result object", result instanceof List<?>);
      List<?> resultList = (List<?>) result;
      Assert.assertEquals("Unexpected cache size", 1, resultList.size());
      Assert.assertEquals("Unexpected cached principal", "admin", resultList.get(0).toString());
      
      // now wait till the assertion has expired and check the authentication cache again.
      Thread.sleep(12000);
      result = server.invoke(name, "getAuthenticationCachePrincipals", methodParams, methodSignature);
      Assert.assertTrue("getAuthenticationCachePrincipals returned an invalid result object", result instanceof List<?>);
      resultList = (List<?>) result;
      Assert.assertEquals("Unexpected cache size", 0, resultList.size());

   }
}
