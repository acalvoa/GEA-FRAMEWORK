<%@page import="gea.tasklist.Tasklist"%>
<%
JspWriter o = out;
String context = request.getRequestURL().toString().replaceAll(request.getServletPath(), "")+"/";
Tasklist task = new Tasklist(o,context, "config.json");
%>
