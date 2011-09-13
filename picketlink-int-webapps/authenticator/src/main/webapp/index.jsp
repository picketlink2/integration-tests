<%@ page import="java.util.*,java.security.*" %>

<%
Principal principal = request.getUserPrincipal();
String name=null;
if(principal != null)
   name = principal.getName();
out.write(name);
%>
