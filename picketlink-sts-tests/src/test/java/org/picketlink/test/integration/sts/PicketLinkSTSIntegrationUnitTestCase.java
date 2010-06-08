/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.picketlink.test.integration.sts;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.w3c.dom.Element;

/**
 * <p>
 * Integration tests for the PicketLink STS
 * </p>
 * @author Anil.Saldhana@redhat.com
 * @since Jun 8, 2010
 */
public class PicketLinkSTSIntegrationUnitTestCase
{
   @Test
   public void testTokenIssue() throws Exception
   {
      //Create a WSTrustClient instance.
      WSTrustClient client = new WSTrustClient("PicketLinkSTS", "PicketLinkSTSPort", 
            "http://localhost:8080/picketlink-sts/PicketLinkSTS", 
            new SecurityInfo("admin", "admin"));
      
      // issue a SAML assertion using the client API.
      Element assertion = null;
      try 
      {
         assertion = client.issueToken(SAMLUtil.SAML2_TOKEN_TYPE);
      }
      catch (WSTrustException wse)
      {
         System.out.println("Unable to issue assertion: " + wse.getMessage());
         wse.printStackTrace();
         System.exit(1);
      }
      
      // print the assertion for demonstration purposes.
      System.out.println("\nSuccessfully issued a standard SAMLV2.0 Assertion!");
      printAssertion(assertion);
 
 
      // Validate the received SAML assertion.
      try
      {
         System.out.println("\n\nIs assertion valid? " + client.validateToken(assertion));
      }
      catch (WSTrustException wse)
      {
         System.out.println("\n\nFailed to validate SAMLV2.0 Assertion: " + wse.getMessage());
         wse.printStackTrace();
      }
   }
   
   private void printAssertion(Element assertion) throws Exception
   {
      TransformerFactory tranFactory = TransformerFactory.newInstance();
      Transformer aTransformer = tranFactory.newTransformer();
      Source src = new DOMSource(assertion);
      Result dest = new StreamResult(System.out);
      aTransformer.transform(src, dest);
   }
}