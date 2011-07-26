<%@ page import="java.io.*,org.picketlink.identity.federation.api.saml.v2.response.SAML2Response,org.picketlink.identity.federation.saml.v2.SAML2Object" %>

<%
InputStream configStream = application.getResourceAsStream("/saml2-response-adfs-claims.xml");
SAML2Response samlResponse = new SAML2Response();
SAML2Object samlObject = samlResponse.getSAML2ObjectFromStream(configStream);

if(samlObject == null)
 throw new RuntimeException("SAML Object is null");

out.println("OK");
%>
