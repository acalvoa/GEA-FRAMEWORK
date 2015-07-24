package gea.gateway;

import gea.adapters.OracleConnector;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class JSONWsp {
	String reque;
	HttpServletRequest req;
	HttpServletResponse res;
	WsRouter router;
	boolean status;
	public JSONWsp(HttpServletRequest req, HttpServletResponse res, String reque, WsRouter rreq) throws IOException{
		this.reque = reque;
		this.req = req;
		this.res = res;
		this.router = rreq;
		Enumeration<String> params = req.getParameterNames();
		while(params.hasMoreElements()){
			String parametro = params.nextElement();
			if(parametro.toUpperCase().equals("WSP")){
				this.processwsp();
				this.status = true;
			}
		}
		this.status = false;
	}
	private void processwsp() throws IOException{
		// TODO Auto-generated method stub
		if(req.getParameter("type") == null || !req.getParameter("type").equals("jsonwsp/request")){
			OracleConnector ora;
			try {
				ora = new OracleConnector();
				String query = "SELECT * FROM GEO_WS_DEFINITION WHERE PATH='"+reque+"'";
				ora.connect();
				ResultSet resultado = ora.query(query);
				PrintWriter out = res.getWriter();
				while(resultado.next()){
					out.println(this.descriptor(resultado.getString("NAME"),resultado.getString("URL"),resultado.getString("TYPES"),resultado.getString("METHODS")));
					return;
				}
				out.println(this.errorState().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				PrintWriter out = res.getWriter();
				out.println(this.errorState().toString());
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				PrintWriter out = res.getWriter();
				out.println(this.errorState().toString());
			}
		}
		else if(req.getParameter("type").equals("jsonwsp/request"))
		{
			Method action;
			Class controlador;
			PrintWriter out = this.res.getWriter();
			try {
				OracleConnector ora = new OracleConnector();
				String query = "SELECT * FROM GEO_WS_DEFINITION WHERE PATH='"+reque+"'";
				ora.connect();
				ResultSet resultado = ora.query(query);
				while(resultado.next()){
					controlador = Class.forName("gea.ws." + router.getClase());
					String metodo = router.getMethod();
					action = controlador.getMethod(metodo, HttpServletRequest.class, HttpServletResponse.class);
					JSONObject param = new JSONObject(req.getParameter("args"));
					Iterator pa  = param.keys();
					while(pa.hasNext()){
						String llave = (String) pa.next();
						req.setAttribute(llave, param.getString(llave));
					}
					JSONObject p = (JSONObject)action.invoke(null,this.req,this.res);
					out.println(this.response(resultado.getString("NAME"), req.getParameter("methodname"), p));
					return;
				}
				out.println(this.errorState().toString());
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.println(this.errorState().toString());
			} 
		}
	}
	private JSONObject errorState(){
		JSONObject retorno = new JSONObject();
		retorno.put("type","jsonwsp/description");
		retorno.put("version", "1.0");
		retorno.put("servicename", "ErrorDescriptor");
		retorno.put("url", "/");
		retorno.put("types", new JSONObject());
		retorno.put("methods", new JSONObject());
		return retorno;
	}
	private JSONObject descriptor(String name, String path, String types, String methods){
		JSONObject retorno = new JSONObject();
		retorno.put("type","jsonwsp/description");
		retorno.put("version", "1.0");
		retorno.put("servicename", name);
		retorno.put("url", path);
		retorno.put("types", new JSONObject(types));
		retorno.put("methods", new JSONObject(methods));
		return retorno;
	}
	private JSONObject response(String name, String method, JSONObject result){
		JSONObject retorno = new JSONObject();
		retorno.put("type","jsonwsp/response");
		retorno.put("version", "1.0");
		retorno.put("servicename", name);
		retorno.put("method", method);
		retorno.put("result", result);
		return retorno;
	}
	public boolean getStatus(){
		return this.status;
	}
}
