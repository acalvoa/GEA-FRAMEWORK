package gea.packet;

import org.java_websocket.WebSocket;


public class Packet202 extends PacketBase{
	//CONSTRUCTOR SIN ARGUMENTOS OBLIGATORIO
	public Packet202() {
		this.code = 202;
	}
	public Packet202(WebSocket conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.code = 202;
	}
	//DEFINIMOS LA LLAMADA ESTATICA
	public static synchronized PacketBase getInstance(WebSocket conn)
	{
		  return new Packet202(conn);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAction() {
		// TODO Auto-generated method stub
	}
	@Override
	public void sendResponse() {
		// TODO Auto-generated method stub
		
	}
}
