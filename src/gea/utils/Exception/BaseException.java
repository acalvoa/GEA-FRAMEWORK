package gea.utils.Exception;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import gea.properties.PropertyManager;

import org.json.JSONObject;

public class BaseException extends Exception{
	JSONObject reg;
	public BaseException(){
		super();
		this.SendToMail();
	}
	public BaseException(String message){
		super(message);
		this.SendToMail();
	}
	public BaseException(String message, JSONObject reg){
		super(message);
		this.reg = reg;
		this.SendToMail();
	}
	public BaseException(Throwable exception){
		super(exception);
		this.SendToMail();
	}
	private void SendToMail(){
		String host = "maiten.contraloria.cl";
		String port = "25";
		String from = "cariatide@contraloria.cl";
		String to = "cariatide@contraloria.cl";
		String subject = "INCIDENCIA - GEOCGR - " + PropertyManager.AMBIENTE_DESPLIEGUE;
		String message = (this.reg != null)?this.getMessage()+"\n\r OBJECT ERROR: "+reg.toString(): this.getMessage();
		String name = "GEOCGR";
		this.sendMail(host,port,from,to,subject,message,name);
	}
	public void sendMail(String host,String port,
	    String from, String to, String subject,
	    String message, String nombreFun) {
		try{
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			Session ses1 = Session.getDefaultInstance(prop, null);
			MimeMessage msg = new MimeMessage(ses1);
			msg.setFrom(new InternetAddress(from));
			InternetAddress destino = new InternetAddress(to);
			destino.setPersonal(nombreFun);
			msg.addRecipient(Message.RecipientType.TO, destino);
			msg.setSubject(subject);
			// CREA
			BodyPart messageBodyPart = new MimeBodyPart();
			// FILL AND 
			messageBodyPart.setContent(message, "text/html");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			msg.setContent(multipart);
			msg.setSentDate(new Date());
			Transport.send(msg);
		} catch(Exception e) {
			
		}
	}
}
