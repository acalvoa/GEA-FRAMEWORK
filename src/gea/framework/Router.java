package gea.framework;

public enum Router {
	// COMIENZO DE ROUTER
	
	// DEFINIMOS LAS RUTAS DEL ROUTER /////////////////////////////////////////////////////////////////////////
	// LA FORMA DE DEFINIR UNA RUTA ES EL NOMBRE DEL CONTROLADOR+ACCION.
	// SI POR EJEMPLO EXISTE UN CONTROLADOR CON EL NOMBRE CONTROLLERTEST Y NECESITAMOS LA ACCION FOUND 
	// DE DICHO CONTROLADOR, LA ACCION RUTEABLE QUEDARA DEFINIDA COMO:
	// TESTFOUND("ControllerTest", "found")
	// SI SOLO QUEREMOS DEFINIR EL CONTROLADOR Y DEJAR QUE EL FRAMEWORK UTILICE EL NOMBRE DE REQUEST COMO METODO
	// DEBEMOS DEFINIR LA RUTA COMO:
	// TEST("ControllerTest", "*")
	// EN DICHA DEFINICION SE DEBE ASIGNAR EL NOMBRE DE LA CLASE Y EL METODO A UTILIZAR LAS DEFINICIONES 
	// POR DEFAULT VIENEN ASIGNADAS EN PETICION DE LA FORMA CONTROLLER/ACTION?QUERYSTRING O CONTROLLER/ACTION
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// RUTAS ASIGNADAS POR FRAMEWORK
	DEFAULT("ControllerDefault", "response"), //RUTA EN CASO DE NO EXISTIR PETICION !IMPORTANTE NO QUITAR.
	// RUTAS ASIGNABLES
	TEST("ControllerDefault", "response");
	// RUTAS ASIGNADAS
	
	// CONFIGURACIONES GLOBALES
	///////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// LA CONFIGURACION PREFIX ES PARA DEFINIR SI PREVIO AL CONTROLADOR SE UTILIZA ALGUN NOMBRE DE PREFIJO
	// SI POR EJEMPLO ASIGAMOS COMO PREFIJO PREFIJO/ TODAS LAS PETICIONES DEL TIPO
	// PREFIJO/CONTROLLER/ACTION SERA TRATADAS COMO CONTROLLER/ACTION
	// ESTA CONFIGURACION SE IGNORA SI ESTE ES UN STRING VACIO
	// ES IMPORNATE QUE DEBE TERMINAR EN / SINO EL FRAMEWORK LO IGNORARA
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String prefix = ""; // DEBE TERMINAR EN /
	
	///////////////////////////
	// FIN CONFIGURACIONES GLOBALES
	
	//DEFINICIONES INTERNAS DEL ROUTER !IMPORTANTE NO MODIFICAR
	private final String clase;
	private final String method;
	//INICIALIZAMOS EL CONSTRUCTOR DEL ROUTER
	Router(String clase, String method){
		this.clase = clase; 
		this.method = method;
	}
	//ESTABLECEMOS LOS METODOS DE ROUTER PARA  ENTRAER LA ACCION
	public String getClase() {
		return clase;
	}
	public String getMethod() {
		return method;
	}
	// FIN ROUTER
}
