package gea.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gea.policies.*;
import gea.controller.*;
import gea.utils.Exception.*;

public class Controller {
	// DEFINIMOS EL CONSTRUCTOR
	public Controller(Request req, Response res) throws Error403Exception, Error500Exception, Error404Exception, ErrorCodeException{
		// TODO Auto-generated constructor stub
		// QUITAMOS LOS PREFIJOS
		if(Router.prefix != ""){
			req.prefix(Router.prefix);
		}
		// RUTEAMOS POR CONTROLADOR Y ACCION
		try{
			Router r = Router.valueOf(req.getController().toUpperCase()+req.getAction().toUpperCase());			
			this.policie(r, req, res);
		}catch(Exception e){
			//RUTEAMOS POR CONTROLADOR
			try{
				res.setCallback(req.getCallback());
				Router r = Router.valueOf(req.getController().toUpperCase());
				this.policie(r, req, res);
			}catch(Error500Exception ex){
				throw new Error403Exception(ex.getMessage());
			}
			catch(Exception ex){
				Router r = Router.valueOf("DEFAULT");
				this.policie(r, req, res);
			}
		}
	}
	private void policie(Router r, Request req, Response res) throws Error403Exception, Error500Exception, Error404Exception, ErrorCodeException{
		Method action;
		//DEFINIMOS EL CONTROLADOR Y LA ACCION A UTILIZAR.
		try {
			Class controlador = Class.forName("gea.controller." + r.getClase());
			String metodo = r.getMethod();
			if(metodo == "*"){
				metodo = req.getAction();
			}
			action = controlador.getMethod(metodo, Request.class, Response.class);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			throw new Error404Exception("Controller or Action not found.");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new Error404Exception("Controller or Action not found.");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new Error404Exception("Controller or Action not found.");
		}
		
		//DEFINIMOS LA POLITICA A UTILIZAR
		Policies p;
		try
		{
			p = Policies.valueOf(r.getClase().toUpperCase());
		}
		catch(Exception e)
		{
			p = Policies.valueOf("DEFAULT");
		}
		
		//APLICAMOS LAS POLITICAS
		try {
			Class c = Class.forName("gea.policies." + p.getPolicie());
			PoliciesBase politica = (PoliciesBase) c.newInstance();
			if(politica.policie(req)){
				politica = null;
				action.invoke(null,req,res);
			}
			else
			{
				throw new Error403Exception("Forbidden Controller");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new Error500Exception("Policie not found.");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new Error500Exception("Policie not found.");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new Error500Exception("Policie not found.");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new Error500Exception("Policie not found.");
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new Error500Exception("Error in action.");
		}
		
	}
}
