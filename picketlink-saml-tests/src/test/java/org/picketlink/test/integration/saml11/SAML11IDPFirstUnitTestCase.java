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
package org.picketlink.test.integration.saml11;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * <p>
 * Unit test the PicketLink IDP application that
 * supports the SAML v1.1 interaction.
 * </p>
 * <p>
 *   <b>Note:</b> This test expects that a set of endpoints that are configured
 *   for the test are available. You may have to start web containers offline
 *   for the endpoints to be live.
 * </p>
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Jul 7, 2011
 */
public class SAML11IDPFirstUnitTestCase
{
   String IDP_URL = System.getProperty( "IDP_URL", "http://localhost:8080/idp/" );
   
   @Test
   public void testSAML11() throws Exception
   {  
      System.out.println("Trying "+ IDP_URL); 
      WebRequest serviceRequest1 = new GetMethodWebRequest( IDP_URL );
      WebConversation webConversation = new WebConversation();
      
      WebResponse webResponse = webConversation.getResponse( serviceRequest1 ); 
      WebForm loginForm = webResponse.getForms()[0];
      loginForm.setParameter("j_username", "tomcat" );
      loginForm.setParameter("j_password", "tomcat" );
      SubmitButton submitButton = loginForm.getSubmitButtons()[0];
      submitButton.click(); 
      
      webResponse = webConversation.getCurrentPage();
      String responseText = webResponse.getText();
      System.out.println("Page=" + responseText);
      assertTrue( " Reached the sales index page ", webResponse.getText().contains( "Sales" ));
      WebLink[] links = webResponse.getLinks();
      boolean foundLink = false;
      for(WebLink webLink: links)
      {
         if( webLink.getURLString().contains("sales-saml11"))
         {
            foundLink = true;
            webResponse = webLink.click();
            assertTrue( " Reached the sales index page ", webResponse.getText().contains( "SalesTool" ));
            break;
         } 
      }
      assertTrue("We found the SP link?", foundLink);
   }
}