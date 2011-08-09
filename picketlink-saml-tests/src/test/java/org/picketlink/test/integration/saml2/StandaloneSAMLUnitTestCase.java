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
package org.picketlink.test.integration.saml2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * SAML Test for Standalone Web Containers
 * @author Anil.Saldhana@redhat.com
 * @since Aug 9, 2011
 */
public class StandaloneSAMLUnitTestCase
{ 
   String SERVICE_1_URL = System.getProperty( "SERVICE_1_URL", "http://localhost:8080/sales-standalone/" );
   String SERVICE_2_URL = System.getProperty( "SERVICE_2_URL", "http://localhost:8080/employee-standalone/" );
   String LOGOUT_URL = "?GLO=true";
   

   public String getService1URL()
   {
      return SERVICE_1_URL;
   }
   public String getService2URL()
   {
      return SERVICE_2_URL;
   }
   
   @Test
   public void testSAMLStandalone() throws Exception
   {
      //Sales Application Login 
      System.out.println("Trying "+ getService1URL());
      WebRequest serviceRequest1 = new GetMethodWebRequest( getService1URL() );
      WebConversation webConversation = new WebConversation();
      
      WebResponse webResponse = webConversation.getResponse( serviceRequest1 ); 
      WebForm loginForm = webResponse.getForms()[0];
      loginForm.setParameter("JBID_USERNAME", "manager" );
      loginForm.setParameter("JBID_PASSWORD", "tomcat" );
      SubmitButton submitButton = loginForm.getSubmitButtons()[0];
      submitButton.click(); 
      
      webResponse = webConversation.getCurrentPage();
      assertTrue( " Reached the sales index page ", webResponse.getText().contains( "SalesTool" ));
      
      //Employee Application Login
      System.out.println("Trying "+ getService2URL());
      webResponse = webConversation.getResponse( getService2URL() );
      assertTrue( " Reached the employee index page ", webResponse.getText().contains( "EmployeeDashboard" ));
      
      //Logout from sales

      System.out.println("Trying "+ getService1URL() + LOGOUT_URL);
      webResponse = webConversation.getResponse( getService1URL() + LOGOUT_URL ); 
      assertTrue( "Reached logged out page", webResponse.getText().contains( "logged" ) );
      
      //Hit the Sales App again
      System.out.println("Trying "+ getService1URL());
      webResponse = webConversation.getResponse( getService1URL() );
      assertTrue( " Reached the Login page ", webResponse.getText().contains( "Login" ));
 
      //Hit the Employee App again
      System.out.println("Trying "+ getService2URL());
      webResponse = webConversation.getResponse( getService2URL() );
      assertTrue( " Reached the Login page ", webResponse.getText().contains( "Login" ));  
   } 
}