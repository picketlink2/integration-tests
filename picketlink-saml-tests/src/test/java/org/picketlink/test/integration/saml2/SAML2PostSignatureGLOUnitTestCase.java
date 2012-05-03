/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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

/**
 * Unit test the GLO scenarios involving two endpoints with SAML2 Post
 * and Signature binding
 * @author anil saldhana
 */
public class SAML2PostSignatureGLOUnitTestCase extends SAML2PostBindingGlobalLogOutUnitTestCase
{
   String SERVICE_5_URL = System.getProperty( "SERVICE_3_URL", "http://localhost:8080/sales-post-sig/" );
   String SERVICE_6_URL = System.getProperty( "SERVICE_4_URL", "http://localhost:8080/employee-sig/" );
   @Override
   public String getService1URL()
   { 
      return SERVICE_5_URL;
   }
   @Override
   public String getService2URL()
   { 
      return SERVICE_6_URL;
   }  
}