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
package org.picketlink.test.integration.openid;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Unit Test the OpenID Consumer App
 * @author Anil.Saldhana@redhat.com
 * @since Jan 25, 2011
 */
public class OpenIDConsumerUnitTestCase 
{
   String SERVICE_1_URL = System.getProperty( "SERVICE_1_URL", "http://localhost:8080/openid-consumer/" ); 
   
   @Test
   public void testOpenIDConsumer() throws Exception
   {   
      final WebClient webClient = new WebClient();
      webClient.setJavaScriptEnabled(true);
      
      final HtmlPage page = (HtmlPage) webClient.getPage( SERVICE_1_URL );
      HtmlForm form = page.getForms().get(0);
      HtmlTextInput textField =  (HtmlTextInput) form.getInputByName( "openid" ); 

      // Change the value of the text field
      textField.setValueAttribute( "http://jbosstest.myopenid.com/" );
      Iterable<HtmlElement> children = form.getAllHtmlChildElements();
      HtmlSubmitInput button = null;
      
      for( HtmlElement elem: children )
      {
         if (elem instanceof HtmlSubmitInput )
         {
            button = (HtmlSubmitInput) elem;
         }
      } 
      
      // Now submit the form by clicking the button and get back the second page.
      HtmlPage page2 = (HtmlPage) button.click();
      form = page2.getForms().get(0); 
      
      HtmlPasswordInput passwordField = (HtmlPasswordInput) form.getInputByName( "password" ); 
      passwordField.setValueAttribute( "jbosstest123" );
      
      button = (HtmlSubmitInput) form.getInputByValue( "Sign In" ); 
      
      page2 = (HtmlPage) button.click();
      System.out.println( "Response after button click on myopenid" ); 
      
      page2 = (HtmlPage) webClient.getPage( SERVICE_1_URL );
      write( page2 );
      String afterLoggingIn = page2.asText();
      assertTrue( afterLoggingIn.contains( "Logged in as http://jbosstest.myopenid.com/" ));
   }
   
   protected void write( HtmlPage page) throws Exception 
   {
      Document doc = page;
      Source source = new DOMSource( doc );
      StringWriter sw = new StringWriter();

      Result streamResult = new StreamResult(sw);
      // Write the DOM document to the stream
      Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "no");
      
      transformer.transform(source, streamResult);
       
      System.out.println( sw.toString() );
   }
}