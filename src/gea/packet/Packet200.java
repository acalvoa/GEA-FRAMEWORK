package gea.packet;

import org.java_websocket.WebSocket;


public class Packet200 extends PacketBase{

	public Packet200() {
		this.code = 200;
	}
	public Packet200(WebSocket conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.code = 200;
	}
	//DEFINIMOS LA LLAMADA ESTATICA
	public static synchronized PacketBase getInstance(WebSocket conn)
	{
		  return new Packet200(conn);
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
