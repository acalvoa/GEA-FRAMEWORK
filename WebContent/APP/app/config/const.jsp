<%@page import="org.json.*"%>
<%@page import="gea.tasklist.Tasklist"%>
<%
	String context = request.getRequestURL().toString().replaceAll(request.getServletPath(), "")+"/";
	JSONObject c = Tasklist.getConfig();
%>
(function(){
	_CONFIG = {
		_WEBSOCKET: {
			HOST: "<% out.print(c.getJSONObject("WEBSOCKET").getString("HOST")); %>",
			PORT: <% out.print(c.getJSONObject("WEBSOCKET").getInt("CLIENTPORT")); %>,
		}
	};
})();
