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
package org.picketlink.test.trust.ws.servlets;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;

import org.picketlink.test.trust.ws.WSTest;
import org.picketlink.trust.jbossws.handler.BinaryTokenHandler;

/**
 * A Servlet that invokes a WS
 * @author Anil.Saldhana@redhat.com
 * @since May 9, 2011
 */
public class TestWSInvokingServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   @SuppressWarnings("rawtypes")
   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   { 
      System.setProperty("binary.http.header", "TEST_HEADER");
      
      URL wsdl = new URL("http://localhost:8080/picketlink-wstest-tests/TestBean?wsdl");
      QName serviceName = new QName("http://ws.trust.test.picketlink.org/", "TestBeanService");
      Service service = Service.create(wsdl, serviceName);
      WSTest port = service.getPort(new QName("http://ws.trust.test.picketlink.org/", "TestBeanPort"), WSTest.class);
 
      BindingProvider bp = (BindingProvider) port;
      List<Handler> handlers = bp.getBinding().getHandlerChain();
      handlers.add(new BinaryTokenHandler());
      bp.getBinding().setHandlerChain(handlers); 
      
      String value = port.echo("Test");
      if( value == null || value.equals("Test") == false)
         throw new ServletException();
   }
}