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
package org.picketlink.test.trust.tests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;

import org.jboss.security.auth.callback.JBossCallbackHandler;
import org.junit.Before;
import org.junit.Test;
import org.picketlink.trust.jbossws.jaas.JBWSTokenIssuingLoginModule;

/**
 * Unit test the {@link JBWSTokenIssuingLoginModule}
 * @author Anil.Saldhana@redhat.com
 * @since Apr 25, 2011
 */
public class JBWSTokenIssuingLoginModuleUnitTestCase
{
   @Test
   public void testLM() throws Exception
   {
      System.setProperty("binary.http.header", "TEST_HEADER");
      LoginContext lc = new LoginContext("test", new JBossCallbackHandler());
      lc.login();
      Subject subject = lc.getSubject();
      assertEquals( 1, subject.getPublicCredentials().size());
   }
   
   @Before
   public void setup() throws Exception
   {
      Configuration.setConfiguration(new Configuration()
      {   
         @Override
         public void refresh()
         { 
         }
         
         @Override
         public AppConfigurationEntry[] getAppConfigurationEntry(String arg0)
         {
            Map<String,Object> options = new HashMap<String,Object>();
            options.put("endpointAddress", "http://localhost:8080/picketlink-sts");
            options.put("wspAppliesTo","http://services.testcorp.org/provider1");

            options.put("serviceName", "PicketLinkSTS");
            options.put("portName", "PicketLinkSTSPort");
            options.put("inject.callerprincipal", "true");
            options.put("groupPrincipalName", "Membership");
           // options.put("handlerChain", "binary"); 
            options.put("username", "UserA");
            options.put("password", "PassA");
            
            AppConfigurationEntry entry = new AppConfigurationEntry(JBWSTokenIssuingLoginModule.class.getName(), 
                  LoginModuleControlFlag.REQUIRED, options); 
            
            return new AppConfigurationEntry[] {entry};
         }
      });
   }
}