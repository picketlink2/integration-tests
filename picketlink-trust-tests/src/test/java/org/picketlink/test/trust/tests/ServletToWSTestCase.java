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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

/**
 * A Test that passes a binary header to a servlet
 * that is governed by the {@code PicketLinkAuthenticator}.
 * The {@code JBWSTokenIssuingLoginModule} is invoked which
 * calls the STS. On the call to the STS, the binary token handler
 * kicks in and reads the binary token and adds it to the sts call.
 * 
 * Once the SAML2 Assertion is obtained, the servlet then makes
 * a call to the WS
 * 
 * @author Anil.Saldhana@redhat.com
 * @since May 9, 2011
 */
public class ServletToWSTestCase
{
   @Test
   public void testServlet2WS() throws Exception
   {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod("http://localhost:8080/binary-test/TestWSInvokingServlet");
      post.addRequestHeader("TEST_HEADER", "somevalue");
      int result = client.executeMethod(post);
      assertEquals(200, result);
   }
}