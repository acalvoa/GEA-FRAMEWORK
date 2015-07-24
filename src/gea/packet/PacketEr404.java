package gea.packet;

import org.java_websocket.WebSocket;

public class PacketEr404 extends PacketBase{

	public PacketEr404(WebSocket conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.code = 404;
	}
	//DEFINIMOS LA LLAMADA ESTATICA
	public static synchronized PacketBase getInstance(WebSocket conn)
	{
		  return new PacketEr404(conn);
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
