package gea.websocket;

import gea.framework.Controller;
import gea.framework.Request;
import gea.framework.Response;
import gea.framework.Session;
import gea.utils.Exception.*;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

public class GEOWebSocketHandler extends WebSocketServer {
	private Hashtable<WebSocket,Session> sesiones = new Hashtable<WebSocket,Session>();
	
	public GEOWebSocketHandler( int port , Draft d ) throws UnknownHostException {
		super( new InetSocketAddress( port ), Collections.singletonList( d ) );
	}
	
	public GEOWebSocketHandler( InetSocketAddress address, Draft d ) {
		super(address, Collections.singletonList( d ) );
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		System.out.println("Hilos Activos: "+Thread.activeCount());
		String hshk = handshake.getFieldValue("sec-websocket-key");
		System.out.println("Handshake Authorized: "+ hshk);
		Response res = null;
		try {
			Session s = new Session(hshk, conn);
			sesiones.put(conn, s);
			res = new Response(conn, this.sesiones);
			res.setPacketType(202);
			res.SendCallback(s.getLogResponse());
			
		} catch (NoSuchAlgorithmException e) {
		} catch (ErrorCodeException e) {
			// TODO Auto-generated catch block
			res.erCode(e.getMessage());
			res = null;
		} catch (Error500Exception e) {
			System.out.println(e.getMessage());
			res.er500();
			res = null;
		}
		System.out.println("Conexiones abiertas: " + sesiones.size() );
	}
	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		System.out.println("Session asociada a handshake "+sesiones.get(conn).getHandshake()+" Cerrada.");
		System.out.println("Hilos Activos: "+Thread.activeCount());
		sesiones.get(conn).save();
		sesiones.remove(conn);
	}

	@Override
	public void onError( WebSocket conn, Exception ex ) {
		System.out.println( "Error:" );
		ex.printStackTrace();
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		Response res = new Response(conn, this.sesiones);
		Controller con;
		try{
			message = sesiones.get(conn).translatePacket(message);
			con = new Controller(new Request(new JSONObject(message), sesiones.get(conn), conn.getReadyState()), res);
		} 
		catch(Error500Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			res.er500();
			res = null;
			con = null;
		}
		catch(Error404Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			res.er404();
			res = null;
			con = null;
		}
		catch(Error403Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			res.er403();
			res = null;
			con = null;
		}
		catch(ErrorCodeException e){
			e.printStackTrace();
			res.erCode(e.getMessage());
			res = null;
			con = null;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			res.er500();
			res = null;
			con = null;
		}
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer blob ) {
		Response res = new Response(conn, this.sesiones);
		res.erCode("600");
	}

	public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
		FrameBuilder builder = (FrameBuilder) frame;
		builder.setTransferemasked( false );
		conn.sendFrame( frame );
	}
}