package gea.packet;

import java.nio.channels.NotYetConnectedException;

import gea.framework.Session;
import gea.utils.Exception.Error500Exception;
import gea.utils.Exception.ErrorCodeException;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class PacketBase {
	//DEFINIMOS LAS VARIABLES QUE DEBEN EXISTIR
	Session session = null;
	protected WebSocket conn;
	JSONObject responseo = null;
	JSONArray responsea = null;
	String callback;
	int code;
	
	//DEFINIMOS LOS METODOS QUE DEBEN SER DEFINIDOS EN LAS CLASE HEREDERAS
	public abstract String toString();
	public abstract void setAction();
	public abstract void sendResponse();
	
	//DEFINIMOS LOS METODOS HEREDABLES
	//DEFINIMOS EL METODO PARA ENVIAR PACKETES PREDETERMINADOS
	//ESTE METODO NO SE VUELVE A DEFINIR
	public final void sendDefaultResponse(){
		this.send(this.makePacket().toString());
	};
	//INSCRIBIMOS EL METODO QUE HACE EL CALLBACK
	public void setCallback(String callback){
		this.callback = callback;
	}
	public void setSession(Session s){
		this.session = s;
	}
	protected void send(String sendp){
		try{
			if(!this.session.isEncrypt()){
				conn.send(sendp);
			}
			else
			{
				conn.send(session.encriptarPacket(sendp));
			}
		}catch(Exception e){
			
		}
	}
	//INDICAMOS EL METODO QUE CREAR EL PACKETE POR DEFECTO
	//ESTE METODO NO SE VUELVE A DEFINIR
	private final JSONObject makePacket(){

		JSONObject json = new JSONObject();
		json.put("code", this.code);
		json.put("callback", this.callback);
		if(this.responseo != null) json.put("response", this.responseo);
		if(this.responsea != null) json.put("response", this.responsea);
		return json;
	}
	//INDICAMOS EL METODO QUE SE UTILIZARA
	public void setResponse(JSONObject response) throws ErrorCodeException{
		try
		{
			this.responseo = response;
			this.responsea = null;
		}
		catch(Exception e)
		{
			throw new ErrorCodeException("601");
		}
	}
	public void setResponse(JSONArray response) throws ErrorCodeException{
		try
		{
			this.responsea = response;
			this.responseo = null;
		}
		catch(Exception e)
		{
			throw new ErrorCodeException("601");
		}
	}
	public void setConn(WebSocket conn){
		this.conn = conn;
	}
}
