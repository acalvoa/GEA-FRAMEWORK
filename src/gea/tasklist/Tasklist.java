package gea.tasklist;

import gea.framework.Model;
import gea.framework.ModelResult;
import gea.model.ModelAuth;
import gea.model.ModelServiceReg;
import gea.model.ModelServicios;
import gea.properties.PropertyManager;
import gea.utils.Exception.Error403Exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;

import javax.servlet.jsp.JspWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Tasklist {
	// ESTABLECEMOS LOS PARAMETROS DE CLASE
	String contexto;
	JspWriter out;
	JSONObject config;
	String layout;
	String task;
	private String parametros;
	private JSONObject jsonparam;
	// ESTABLECEMOS EL CONSTRUCTOR
	public Tasklist(JspWriter out, String contexto, String task) throws IOException {
		super();
		this.out = out;
		this.contexto = contexto;
		this.layout = "";
		this.task = task;
		this.grunt();
	}
	public Tasklist(JspWriter out, String contexto, String parametros, String task) throws IOException {
		super();
		this.out = out;
		this.contexto = contexto;
		this.parametros = parametros;
		this.layout = "";
		this.task = task;
		this.grunt();
	}
	public Tasklist(JspWriter out, String contexto, JSONObject parametros, String task) throws IOException {
		super();
		this.out = out;
		this.task = task;
		this.contexto = contexto;
		this.jsonparam = parametros;
		this.layout = "";
	}
	public void chileGrunt() throws IOException, Error403Exception, InstantiationException, IllegalAccessException, JSONException, ParseException{
		Model<ModelAuth> auth = new Model<ModelAuth>(ModelAuth.class);
		ModelResult<ModelAuth> resul = auth.findOne(this.jsonparam.toString());
		if(resul.size() > 0){
			JSONArray resp = resul.toJSON();
			for(int i=0; i<resp.length(); i++){
				JSONObject key = resp.getJSONObject(i);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				Date exp= format.parse(key.getString("FINISHTIME"));
				Date create = format.parse(key.getString("CREATETIME"));
				Date now = new Date();
				if(/*now.after(create) && */now.before(exp)){
					this.loadConfig();
					this.loadLayout();
					this.loadCrypto();
					this.loadDEP();
					this.loadGea();
					this.loadStyles();
					this.loadLayouts();
					this.loadControllers(); 
					this.printLayout();
				}
				else
				{
					this.loadConfig();
					this.ERROR("La llave indicada para la licitaci&oacute;n expiro, Intente nuevamente desde el formulario de ChileCompra<br>"+now.toString());
					this.printLayout();
				}
				break;
			}
		}
		else{
			this.loadConfig();
			this.ERROR("La licitaci&oacute;n indicada no posee una llave valida publica en los registros de GEOCGR, Intente nuevamente desde el formulario de ChileCompra");
			this.printLayout();
		}
	}
	public void chileGruntNotValidate() throws IOException, Error403Exception, InstantiationException, IllegalAccessException, JSONException, ParseException{
		Model<ModelAuth> auth = new Model<ModelAuth>(ModelAuth.class);
		ModelResult<ModelAuth> resul = auth.findOne(this.jsonparam.toString());
		if(resul.size() > 0){
			JSONArray resp = resul.toJSON();
			for(int i=0; i<resp.length(); i++){
				JSONObject key = resp.getJSONObject(i);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
				Date exp= format.parse(key.getString("FINISHTIME"));
				Date create = format.parse(key.getString("CREATETIME"));
				Date now = new Date();
				if(true){
					this.loadConfig();
					this.loadLayout();
					this.loadCrypto();
					this.loadDEP();
					this.loadGea();
					this.loadStyles();
					this.loadLayouts();
					this.loadControllers(); 
					this.printLayout();
				}
				else
				{
					this.loadConfig();
					this.ERROR("La llave indicada para la licitaci&oacute;n expiro, Intente nuevamente desde el formulario de ChileCompra<br>"+now.toString());
					this.printLayout();
				}
				break;
			}
		}
		else{
			this.loadConfig();
			this.ERROR("La licitaci&oacute;n indicada no posee una llave valida publica en los registros de GEOCGR, Intente nuevamente desde el formulario de ChileCompra");
			this.printLayout();
		}
	}
	private void grunt() throws IOException{
		this.loadConfig();
		this.loadLayout();
		this.loadCrypto();
		this.loadDEP();
		this.loadGea();
		this.loadStyles();
		this.loadLayouts();
		this.loadControllers(); 
		this.printLayout();
	}
	private void loadCrypto(){
		String a = "";
		JSONArray json = this.config.getJSONArray("CRYPTO");
		for(int i=0; i< json.length(); i++){
			a+= "<script src='"+this.config.getJSONObject("ASSET").getString("CRYPTO")+json.getString(i)+".js'></script>\r\n";
		}
		this.layout = layout.replaceAll("<!-- CRYPTO BEGIN -->.*<!-- CRYPTO END -->", "<!-- CRYPTO BEGIN -->\r\n"+a+"<!-- CRYPTO END -->");
	}
	private void loadGea(){
		String a = "";
		JSONArray json = this.config.getJSONArray("GEA");
		for(int i=0; i< json.length(); i++){
			a+= "<script src='"+this.config.getJSONObject("ASSET").getString("GEA")+json.getString(i)+"/"+json.getString(i)+".js'></script>\r\n";
		}
		this.layout = layout.replaceAll("<!-- GEA BEGIN -->.*<!-- GEA END -->", "<!-- GEA BEGIN -->\r\n"+a+"<!-- GEA END -->");
	}
	private void loadDEP(){
		//LOAD DEPENDENCE
		String depin = "";
		String depout = "";
		String cssdep = "";
		String mp ="";
		JSONObject json = this.config.getJSONObject("DEPENDENCE");
		//LOAD DEPIN
		JSONArray jsondepin = json.getJSONArray("INTERNAL");
		for(int i=0; i< jsondepin.length(); i++){
			depin += "<script src='"+this.config.getJSONObject("ASSET").getString("DEPENDENCE")+jsondepin.getString(i)+"/"+jsondepin.getString(i)+".js'></script>\r\n";
		}
		//LOAD DEPOUT
		JSONArray jsondepout = json.getJSONArray("EXTERNAL");
		for(int i=0; i< jsondepout.length(); i++){
			depout += "<script src='http://"+jsondepout.getString(i)+"'></script>\r\n";
		}
		
		//LOAD CSSDEP
		JSONArray jsondepcss = json.getJSONArray("CSS");
		for(int i=0; i< jsondepcss.length(); i++){
			cssdep += "<link rel='stylesheet' type='text/css' href='"+this.config.getJSONObject("ASSET").getString("CSSDEPENDENCE")+jsondepcss.getString(i)+"/"+jsondepcss.getString(i)+".css' />\r\n";
		}
		//REEMPLAZAMOS EN EL LAYOUT		
		this.layout = layout.replaceAll("<!-- DEPEXT BEGIN -->.*<!-- DEPEXT END -->", "<!-- DEPEXT BEGIN -->\r\n"+depout+"<!-- DEPEXT END -->");
		this.layout = layout.replaceAll("<!-- DEPIN BEGIN -->.*<!-- DEPIN END -->", "<!-- DEPIN BEGIN -->\r\n"+depin+"<!-- DEPIN END -->");
		this.layout = layout.replaceAll("<!-- CSSDEP BEGIN -->.*<!-- CSSDEP END -->", "<!-- CSSDEP BEGIN -->\r\n"+cssdep+"<!-- CSSDEP END -->");
	}
	private void loadLayouts(){
		String web = "";
		JSONArray json = this.config.getJSONArray("LAYOUT");
		for(int i=0; i< json.length(); i++){
			try {
		         URL url = new URL(this.contexto+this.config.getJSONObject("ASSET").getString("HTML")+json.getString(i)+"?"+this.parametros);
		         // Volcamos lo recibido al buffer
		         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		         String inputLine;
		         while ((inputLine = in.readLine()) != null) {
		             web += inputLine+"\r\n";
		        }
		      }
		      catch (IOException e) {
		         e.printStackTrace();
		      }
		}
		web = Matcher.quoteReplacement(web); 
		this.layout = layout.replaceAll("<!-- PARTIAL HTML BEGIN -->.*<!-- PARTIAL HTML END -->", "<!-- PARTIAL HTML BEGIN -->\r\n"+web+"<!-- PARTIAL HTML END -->");
	}
	private void ERROR(String error) throws JSONException, IOException{
		URL jsonconf = new URL(this.contexto+this.config.getJSONObject("ASSET").getString("ERROR"));
        BufferedReader in = new BufferedReader(
        new InputStreamReader(jsonconf.openStream()));
        String line;
        while ((line = in.readLine()) != null){
            this.layout += line;
	    }
        in.close(); 
        this.layout = layout.replaceAll("<!-- ERROR BEGIN -->.*<!-- ERROR END -->", error);
	}
	private void loadStyles(){
		String styles = "";
		JSONArray json = this.config.getJSONArray("STYLES");
		for(int i=0; i< json.length(); i++){
			styles += "<link rel='stylesheet/less' type='text/css' href='"+this.config.getJSONObject("ASSET").getString("STYLES")+json.getString(i)+"' />\r\n";
		}
		this.layout = layout.replaceAll("<!-- STYLES BEGIN -->.*<!-- STYLES END -->", "<!-- STYLES BEGIN -->\r\n"+styles+"<!-- STYLES END -->");
	}
	private void loadControllers(){
		String controller = "";
		JSONArray json = this.config.getJSONArray("CONTROLLER");
		for(int i=0; i< json.length(); i++){
			controller += "<script src='"+this.config.getJSONObject("ASSET").getString("CONTROLLER")+json.getString(i)+".js'></script>\r\n";
		}
		this.layout = layout.replaceAll("<!-- CONTROLLER BEGIN -->.*<!-- CONTROLLER END -->", "<!-- CONTROLLER BEGIN -->\r\n"+controller+"<!-- CONTROLLER END -->");
	}  
	private void loadConfig() throws IOException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream stream = classLoader.getResourceAsStream(this.task);
	    PropertyManager pm = PropertyManager.getInstance();
	    //LEEMOS EL ARCHIVO DE CONFIGURACION
	    byte[] contents = new byte[4096];
	    int bytesRead=0;
	    String config = ""; 
	    while( (bytesRead = stream.read(contents)) != -1){ 
	      String linea = new String(contents, 0, bytesRead);
	      if(linea.trim().indexOf("//") == 0){
	    	  continue;
	      }
	      config += linea;
	    }
	    config = config.replaceAll("//.*[\r\n]", "");
	    try
	    {
	    	this.config = new JSONObject(config);
	    	JSONObject db = new JSONObject();
	    	db.put("HOST", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBHOST).toString());
	    	db.put("PORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBPORT).toString());
	    	db.put("USERNAME", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBUSERNAME).toString());
	    	db.put("PASSWORD", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBPASSWORD).toString());
	    	db.put("DATABASE", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBDATABASE).toString());
	    	JSONObject app = new JSONObject();
	    	app.put("HOST", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.APPHOST).toString());
	    	app.put("PORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.APPPORT).toString());
	    	app.put("CLIENTPORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.CLIENTPORT).toString());
	    	this.config.put("DATABASE",db);
	    	this.config.put("WEBSOCKET", app);
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Existe un error en la hoja de configuraciones del portal config.json");
	    }
	}
	private void printLayout() throws IOException{
		out.println(this.layout);
	}
	public static JSONObject getConfig(String con) throws IOException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream stream = classLoader.getResourceAsStream(con);
	    //LEEMOS EL ARCHIVO DE CONFIGURACION
	    byte[] contents = new byte[4096];
	    int bytesRead=0;
	    String config = ""; 
	    while( (bytesRead = stream.read(contents)) != -1){ 
	      String linea = new String(contents, 0, bytesRead);
	      if(linea.trim().indexOf("//") == 0){
	    	  continue;
	      }
	      config += linea;
	    }
	    //ELIMINAMOS LOS COMENTARIOS
	    config = config.replaceAll("//.*[\r\n]", "");
	    try
	    {
	    	PropertyManager pm = PropertyManager.getInstance();
	    	JSONObject configu = new JSONObject(config);
	    	JSONObject db = new JSONObject();
	    	db.put("HOST", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBHOST).toString());
	    	db.put("PORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBPORT).toString());
	    	db.put("USERNAME", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBUSERNAME).toString());
	    	db.put("PASSWORD", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBPASSWORD).toString());
	    	db.put("DATABASE", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBDATABASE).toString());
	    	JSONObject app = new JSONObject();
	    	app.put("HOST", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.APPHOST).toString());
	    	app.put("PORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.APPPORT).toString());
	    	app.put("CLIENTPORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.CLIENTPORT).toString());
	    	configu.put("DATABASE",db);
	    	configu.put("WEBSOCKET", app);
	    	return configu;
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Existe un error en la hoja de configuraciones del portal config.json");
	    	return null;
	    }
	}
	public static JSONObject getConfig() throws IOException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream stream = classLoader.getResourceAsStream("config.json");
	    //LEEMOS EL ARCHIVO DE CONFIGURACION
	    byte[] contents = new byte[4096];
	    int bytesRead=0;
	    String config = ""; 
	    while( (bytesRead = stream.read(contents)) != -1){ 
	      String linea = new String(contents, 0, bytesRead);
	      if(linea.trim().indexOf("//") == 0){
	    	  continue;
	      }
	      config += linea;
	    }
	    //ELIMINAMOS LOS COMENTARIOS
	    config = config.replaceAll("//.*[\r\n]", "");
	    try
	    {
	    	PropertyManager pm = PropertyManager.getInstance();
	    	JSONObject configu = new JSONObject(config);
	    	JSONObject db = new JSONObject();
	    	db.put("HOST", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBHOST).toString());
	    	db.put("PORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBPORT).toString());
	    	db.put("USERNAME", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBUSERNAME).toString());
	    	db.put("PASSWORD", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBPASSWORD).toString());
	    	db.put("DATABASE", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.DBDATABASE).toString());
	    	JSONObject app = new JSONObject();
	    	app.put("HOST", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.APPHOST).toString());
	    	app.put("PORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.APPPORT).toString());
	    	app.put("CLIENTPORT", pm.getProperty(PropertyManager.GEOCGR_FILE,PropertyManager.GEOCGR.CLIENTPORT).toString());
	    	configu.put("DATABASE",db);
	    	configu.put("WEBSOCKET", app);
	    	return configu;
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Existe un error en la hoja de configuraciones del portal config.json");
	    	return null;
	    }
	}
	
	private void loadLayout() throws IOException{
		URL jsonconf = new URL(this.contexto+this.config.getJSONObject("ASSET").getString("LAYOUT"));
        BufferedReader in = new BufferedReader(
        new InputStreamReader(jsonconf.openStream()));
        String line;
        while ((line = in.readLine()) != null)
           this.layout += line;
        in.close();
	}
	public static int getPort() throws IOException{
		JSONObject c = Tasklist.getConfig();
		return c.getJSONObject("WEBSOCKET").getInt("PORT");
	}
	
}
