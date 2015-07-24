package gea.gateway;

import gea.framework.Request;
import gea.framework.Response;
import gea.framework.Router;
import gea.utils.Exception.Error404Exception;
import gea.utils.Exception.ErrorCodeException;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class Gateway
 */
public class Gateway extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Gateway() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.process("GET",request,response);
	}	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.process("POST",request,response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.process("DELETE",request,response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.process("PUT",request,response);
	}
	
	
	private void process(String typeReq, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Method action;
		//DEFINIMOS EL CONTROLADOR Y LA ACCION A UTILIZAR.
		WsRouter r;
		PrintWriter out = response.getWriter();
		String req = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath()+"/", "").toUpperCase();
		try{
			r = WsRouter.valueOf(req);
		}
		catch(IllegalArgumentException e){
			r = WsRouter.valueOf("DEFAULT");
		}
		JSONWsp wsp = new JSONWsp(request,response,req,r);
		if(!wsp.getStatus()){
			if(r.validateType(typeReq)){
				Class controlador;
				try {
					controlador = Class.forName("gea.ws." + r.getClase());
					String metodo = r.getMethod();
					action = controlador.getMethod(metodo, HttpServletRequest.class, HttpServletResponse.class);
					JSONObject p = (JSONObject)action.invoke(null,request,response);
					out.println(p.toString());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					this.res404(response);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					this.res404(response);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					this.res404(response);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					this.res500(e.getMessage(),response);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					this.res500(e.getMessage(),response);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					this.res500(e.getMessage(),response);
				} 
			
			}
			else
			{
				this.res403(response);
			}
		}
	}
	private void res404(HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("CODE", 404);
		res.put("STATUS", "Web Service not Found");
		out.println(res.toString());
	}
	private void res403(HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("CODE", 403);
		res.put("STATUS", "WS Action forbidden");
		out.println(res.toString());
	}
	private void res500(String error,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("CODE", 500);
		res.put("STATUS", "Internal Server Error");
		res.put("ERROR",error);
		out.println(res.toString());
	}
	private void resCode(int code, String message,HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("CODE", code);
		res.put("STATUS", "Internal Server Error");
		res.put("ERROR", message);
		out.println(res.toString());
	}
}
