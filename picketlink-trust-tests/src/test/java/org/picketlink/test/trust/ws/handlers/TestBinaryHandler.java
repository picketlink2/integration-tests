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
package org.picketlink.test.trust.ws.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jboss.wsf.common.handler.GenericSOAPHandler;
import org.picketlink.trust.jbossws.Constants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A Test {@link GenericSOAPHandler} that just verifies that the 
 * SOAP header has a wsse Binary Security Token before letting the call
 * go through.
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Apr 5, 2011
 */
@SuppressWarnings("rawtypes")
public class TestBinaryHandler extends GenericSOAPHandler
{ 
   private static Set<QName> headers;

   static
   {
      HashSet<QName> set = new HashSet<QName>();
      set.add(Constants.WSSE_HEADER_QNAME);
      headers = Collections.unmodifiableSet(set);
   }

   public Set<QName> getHeaders()
   {
      //return a collection with just the wsse:Security header to pass the MustUnderstand check on it
      return headers;
   }
   
   @Override
   protected boolean handleInbound(MessageContext msgContext)
   { 
      SOAPMessageContext soapMessageContext = (SOAPMessageContext) msgContext;
      SOAPMessage soap = soapMessageContext.getMessage();
      try
      {
         soap.writeTo(System.out);
         SOAPHeader header = soap.getSOAPHeader(); 
         Iterator iter = header.extractAllHeaderElements();
         if( iter != null)
         {
            while(iter.hasNext())
            {
               SOAPHeaderElement headerEl = (SOAPHeaderElement) iter.next();
               if(headerEl.getNodeName().contains(Constants.WSSE_LOCAL))
               {
                  NodeList nl = headerEl.getChildNodes();
                  for( int i = 0; i < nl.getLength(); i++)
                  {
                     Node n = nl.item(i);
                     if( n.getNodeName().contains(Constants.WSSE_BINARY_SECURITY_TOKEN))
                        return true;
                  }
               }
            }
         }
      }
      catch (Exception e)
      { 
         e.printStackTrace();
      }  
      return false;
   }
}