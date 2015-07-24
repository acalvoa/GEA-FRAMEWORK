package gea.websocket;

import gea.properties.PropertyManager;
import gea.tasklist.Tasklist;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.CloseFrame;

/**
 * Servlet implementation class WebSocket
 */
public class WebSocket extends HttpServlet {
	private static final long serialVersionUID = 1L;
    protected GEOWebSocketHandler servidor;
    /**
	* @see HttpServlet#HttpServlet()
	*/
    public WebSocket() {
        super();
        // TODO Auto-generated constructor stub
    }
	public void init(ServletConfig config) {
		//EJECUTAMOS EL WEBSOCKET
		WebSocketImpl.DEBUG = false;
		int port;
		try {
			port = Tasklist.getPort();
		} catch ( Exception e ) {
			System.out.println("No port specified. Defaulting to 11750");
			port = 11750;
		}
		try {
			this.servidor = new GEOWebSocketHandler( port, new Draft_17() );
			servidor.start();
			System.out.println("Servicio WebSocket iniciado en el puerto "+port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Existe un error al iniciar el servidor WebSocket");
		}
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			this.servidor.stop();
			System.out.println("Servicio WebSocket apagado");			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Enviando as todas las conecciones stop response");
		} catch (InterruptedException e) {
			System.out.println("Enviando a todas las conecciones stop response");
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Enviando a todas clas conecciones stop response");
		}
		super.destroy();
	}

}
