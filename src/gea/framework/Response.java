package gea.framework;

import java.nio.channels.NotYetConnectedException;
import java.util.Enumeration;
import java.util.Hashtable;

import gea.packet.*;
import gea.utils.Exception.Error500Exception;
import gea.utils.Exception.ErrorCodeException;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

public class Response {
	WebSocket conn;
	String callback;
	private Hashtable<WebSocket,Session> sesiones;
	PacketBase packet;
	
	public Response(WebSocket conn, Hashtable<WebSocket,Session> sesiones){
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.sesiones = sesiones;
		this.setPacketType(200);
	}
	public void setPacketType(int code){
		try {
			packet = (PacketBase) Class.forName("gea.packet.Packet"+code).newInstance();
			packet.setConn(this.conn);
			packet.setSession(sesiones.get(this.conn));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al inicializar packet de repuesta");
			this.er500();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al inicializar packet de repuesta");
			this.er500();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al inicializar packet de repuesta");
			this.er500();
		}
	}
	public void er500(){
		// TODO Auto-generated method stub
		PacketEr500.getInstance(conn).sendDefaultResponse();
	}
	public void erCode(String code){
		// TODO Auto-generated method stub
		PacketErCode.getInstance(conn, code).sendDefaultResponse();
	}
	public void er403() {
		// TODO Auto-generated method stub
		PacketEr403.getInstance(conn).sendDefaultResponse();
	}
	public void er404(){
		// TODO Auto-generated method stub
		PacketEr404.getInstance(conn).sendDefaultResponse();
	}
	public void broadcast(String message) {
		Enumeration<WebSocket> con = sesiones.keys();
		while(con.hasMoreElements()){
			WebSocket w = con.nextElement();
			w.send(message);
		}
	}
	public void SendCallback(JSONObject response) throws ErrorCodeException{
		packet.setCallback(callback);
		packet.setResponse(response);
		packet.sendDefaultResponse();
	}
	public void SendCallback(JSONArray response) throws ErrorCodeException{
		packet.setCallback(callback);
		packet.setResponse(response);
		packet.sendDefaultResponse();
	}
	public void Send(){
		
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}

}
