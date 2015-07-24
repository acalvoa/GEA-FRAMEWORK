package gea.packet;

import org.java_websocket.WebSocket;


public class PacketErCode extends PacketBase{

	public PacketErCode(WebSocket conn, int code) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.code = code;
	}
	//DEFINIMOS LA LLAMADA ESTATICA
	public static synchronized PacketBase getInstance(WebSocket conn, String code)
	{
		  return new PacketErCode(conn, new Integer(code));
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
