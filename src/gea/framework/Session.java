package gea.framework;

import gea.utils.Exception.Error500Exception;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

import javassist.expr.NewArray;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

public class Session {
	///////////////////////////////////////////////////////////////////////////////////////////////
	// CONTIENE INFORMACIÓN DE LOS CLIENTES CONECTADOS
	// CADA VEZ QUE UN CLIENTE INICIA COMUNICACION CON EL WEBSOCKET
	// ESTE ENTREGA UN HANDSHAKE AL SERVIDOR EL CUAL ASOCIARA ESE AL OBJETO DE SESSION 
	// HASTA QUE ESTE SE DESCONECTE. ESTE OBJETO POSEE TODA LA INFORMACION REFERENTE AL USUARIO
	///////////////////////////////////////////////////////////////////////////////////////////////
	boolean authenticated;
	Date date;
	Hashtable<String,String> SESSION;
	String handshake;
	WebSocket conection;
	JSONArray flow;
	boolean save;
	Key llave;
	boolean Encrypt;
	//DEFINIMOS EL CONSTRUCTOR
	public Session(String handshake, WebSocket conn) throws NoSuchAlgorithmException, Error500Exception {
		// TODO Auto-generated constructor stub
		this.save = false;
		this.authenticated = false;
		this.date = new Date();
		this.SESSION = new Hashtable<String,String>();
		this.handshake = handshake;
		this.conection = conn;
		this.flow = new JSONArray();
		this.Encrypt = false;
		this.setKey();
	}
	//METODOS PUBLICOS DE SESSION
	public boolean isAuthenticated() {
		return this.authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	public Date getDate() {
		return this.date;
	}
	public String getHandshake() {
		return this.handshake;
	}
	public String getSessionVar(String var){
		return this.SESSION.get(var);
	}
	public void setSessionVar(String key, String value){
		this.SESSION.put(key, value);
	}
	public void SetAction(String controller, String action, String data){
		JSONObject fin = new JSONObject();
		fin.put("controller", controller);
		fin.put("action", action);
		fin.put("data", data);
		this.flow.put(fin);
	}
	public void save(){
		this.save = true;
		System.out.println("Guardando sesion de usuario: "+this.handshake);
	}
	protected void finalize() throws Throwable{
		if(!this.save){
			this.save();
		}
	}
	private void setKey() throws NoSuchAlgorithmException, Error500Exception{
		BigInteger l = BigInteger.probablePrime(127, new Random());
		BigInteger p = new BigInteger(127, l.intValue(), new Random());
		this.llave = new Key(p);
	}
	public JSONObject getLogResponse(){
		JSONObject json = new JSONObject();
		json.put("key", llave.getPublicKey());
		json.put("p", llave.getP());
		json.put("g", llave.getG());
		json.put("date", this.getDate().toString());
		return json;
	}
	public void generateClave(String publica) throws Error500Exception{
		if(this.llave.CalClave(publica)){
			this.Encrypt = true;
		}
	}
	public String translatePacket(String text) throws Error500Exception{
		if(this.Encrypt){
			return llave.decrypt(text);
		}
		else
		{
			return text;
		}
	}
	public String encriptarPacket(String text) throws Error500Exception{
		return this.llave.encrypt(text);
	}
	public boolean isEncrypt() {
		return Encrypt;
	}
}
class Key{
	String key;
	BigInteger p,a,A;
	BigInteger g = new BigInteger("2",10);
	Aes aes = null;
	public Key(BigInteger p) throws Error500Exception{
		try
		{
			this.p = p;
			this.makea();
		}
		catch(Exception e){
			throw new Error500Exception("Error al setear la clave simetrica");
		}
	}
	private void makea(){
		BigInteger l = BigInteger.probablePrime(126, new Random());
		BigInteger a = new BigInteger(126, l.intValue(), new Random());
		this.a = a;
	}
	public String getPublicKey(){
		return g.modPow(a, p).toString();
	}
	public String getP(){
		return p.toString();
	}
	public String getG(){
		return g.toString();
	}
	public boolean CalClave(String publica) throws Error500Exception{
		BigInteger B = new BigInteger(publica);
		this.key = B.modPow(this.a, p).toString();
		aes = new Aes(this.key);
		return true;
	}
	public String decrypt(String text) throws Error500Exception {
		try
		{
			if(aes != null){
				return aes.decrypt(text);
			}
			else
			{
				throw new Error500Exception("El objeto de cifrado no ha sido definido en tiempo de ejecución - Session.java");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			throw new Error500Exception("Error al desencriptar el packet cifrado - Session.java");
		}
	}
	public String encrypt(String text) throws Error500Exception{
		try
		{
			if(aes != null){
				return aes.encrypt(text);
			}
			else
			{
				throw new Error500Exception("El objeto de cifrado no ha sido definido en tiempo de ejecución - Session.java");
			}
		}
		catch(Exception e)
		{
			throw new Error500Exception("Error al encriptar el packet cifrado - Session.java");
		}
	}
	
}