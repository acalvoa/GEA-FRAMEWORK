// APP BEGIN
(function(){
	// DEFINIMOS LA VARIABLES DE ENTORNO
	// Y SUBRUTINAS DEL PAQUETE COM WEBSOCKET
	COM = function(config, ready){
		var readyState = false;
		var self = this;
		//////////////////////////////////////////////////////////////////////////////////////////////
		//	DEFINIMOS EL STACK DE CALLBACKS
		//	SE DEFINE EL OBJETO QUE MANTIENE LOS CALLBACKS
		//  PARA ESTE OBJETO EXISTEN 4 METODOS
		// 	ADDCALLBACK
		//	GETCALLBACK
		//	REMOVECALLBACK
		//	CLEARCALLBACK
		//////////////////////////////////////////////////////////////////////////////////////////////
		var stacks = function(){
			var stack = {};
			var searchCallback = function(callback){
				if(typeof stack[callback] == "undefined"){
					return false;
				}
				else
				{
					return true;
				}
			};
			this.addManualCallback = function(hash, callback){
				if(!searchCallback(hash)){
					stack[hash] = callback;
					return true;
				}
				else
				{
					return false;
				}
			}
			this.addCallback = function(callback){
				var hash = Math.random().toString(36).substring(0);
				if(!searchCallback(hash)){
					stack[hash] = callback;
					return hash;
				}
				else
				{
					return addCallback(callback);
				}
			};
			this.getCallback = function(callback){
				if(searchCallback(callback)){
					return stack[callback];
				}
				else
				{
					return false;
				}
			};
			this.removeCallback = function(callback){
				delete stack[callback];
			};	
			this.clearCallback = function(callback){
				console.log("Limpiando Stack de procesos");
				var stack = {};
			};
		};
		// /////////////////////////////////////////////////////////////
		// DEFINIMOS UNA COLA DE ESPERA
		// 
		// /////////////////////////////////////////////////////////////
		var queued = function(){
			var that= this;
			var list = [];
			var watchdog = null;
			this.queued = function(obj){
				list.push(obj);
				if(watchdog == null) that.listen();
			}
			this.unqueued = function(i){
				if(list.length > 0){
					var req = list[i];
					var resp = self.request(req, true);
					if(resp){
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			this.process = function(){
				clearInterval(watchdog);
				watchdog = null;
				for(i=0; i<list.length; i++){
					if(!that.unqueued(i)){
						i--;
					};
				}
				list = [];
			}
			this.listen = function(){
				watchdog = setInterval(that.check,1000);
			}
			this.check = function(){
				if(readyState){
					that.process();
				}
			}
		};
		//////////////////////////////////////////////////////////////////////////////////////////////
		// DEFINIMOS EL BUFFER

		var databank = function(){
			var buffer = {};
			var stack = {};
			this.query = function(tquery, tdata, theaders){
				var hash = CryptoJS.MD5(tquery+JSON.stringify(tdata)+JSON.stringify(theaders));
				if(typeof buffer[hash] != "undefined"){
					return buffer[hash];
				} 
				else
				{
					return false;
				}
			};
			this.regbuffer = function(tquery, tdata, theaders, callback){
				var hash = CryptoJS.MD5(tquery+JSON.stringify(tdata)+JSON.stringify(theaders));
				stack[callback] = hash;
			};
			this.bufferer = function(callback, obj){
				if(typeof stack[callback] != "undefined"){
					buffer[stack[callback]] = obj;
					delete stack[callback];
				}
			};
		};

		//////////////////////////////////////////////////////////////////////////////////////////////
		// DEFINIMOS LA VARIABLE QUE GUARDA LA CONEXION CON EL SERVIDOR Y SESSION
		var self = this;
		var ws;
		var stack;
		var done;
		var qued;
		var aes = null;
		var buffer = null;
		//////////////////////////////////////////////////////////////////////////////////////////////
		//	DEFINIMOS LOS CODIGOS DE OPERACIÓN WEBSOCKET
		var codeHandler = {
			on404: function(packet){
				alert("404 - Action not found");
			},
			on500: function(packet){
				alert();
			},
			on403: function(packet){},
			on200: function(packet){
				if(typeof packet.callback != undefined){
					buffer.bufferer(packet.callback, packet.response);
					var callback = stack.getCallback(packet.callback);
					var after = done.getCallback(packet.callback);
					if(callback != false){
						callback(packet.response);
						if(after != false) after();
					}
					else
					{
						console.log("No existe callback en la lista de procesos.")
						if(after != false) after();
					}
				}
			},
			on202: function(packet){
				var a = randTruePrime(126);
				var A = powMod(str2bigInt(packet.response.g,10,80),a,str2bigInt(packet.response.p,10,80));
				requestlog({
					code: 202,
					request: "user/log", 
					callback: function(res){
						console.log("Sesión iniciada - "+packet.response.date);
						console.log("Llave publica - Recibida");
						readyState = true;
						self.onReady();
					},
					data:{
						a:bigInt2str(A,10)
					}
				});
				aes = new AesUtil(bigInt2str(powMod(str2bigInt(packet.response.key,10,80),a,str2bigInt(packet.response.p,10,80)),10));
			}
		};
		//////////////////////////////////////////////////////////////////////////////////////////////
		// DEFINIMOS EL CONSTRUCTOR DEL WEBSOCKET QUE ESTABLECE LA CONEXION INICIAL CON EL SERVIDOR
		//////////////////////////////////////////////////////////////////////////////////////////////
		var constructor = function(onready){
			console.log("Conectando al servidor GeoCGR...");
			ws = new WebSocket("ws://"+config.websocket.host+":"+config.websocket.port+"/");
			stack = new stacks();
			done = new stacks();
			qued = new queued();
			buffer = new databank();
			ws.onopen = onopen
			ws.onmessage = onmessage;
			ws.onclose = onclose;
			ws.onerror = onerror;
			self.onReady = onready;
		};
		///////////////////////////////////////////////////////////////
		//  NO MODIFICAR
		//	DEFINIMOS LOS EVENTOS DEL WEBSOCKET
		//	WS.ONOPEN    -> ACCION DE APERTURA DE CONEXION
		//	WS.ONMESSAGE -> ACCION DE RECEPCION DE MENSAJE
		//	WS.ONCLOSE 	 -> ACCION DE CIERRE DE CONEXION POR EL SERVIDOR
		//	WS.ONERROR   -> EVENTO DE INTERRUPCION FRENTE A UN ERROR
		///////////////////////////////////////////////////////////////
		var onopen = function() {
			console.log("Conexion establecida, ejecutando subrutinas.");
			//ACCIONES POST CONEXION
		};

		var onmessage = function (packet) {
			var cont;
			if(aes != null){
				cont = JSON.parse(aes.decrypt(packet.data));
			}
			else
			{
				cont = JSON.parse(packet.data);
			}
			if(
				typeof codeHandler["on"+cont.code] == "undefined"){
				console.log("Error code Handler");
			}
			else
			{
				codeHandler["on"+cont.code](cont);
			}
		};

		var onclose = function() {
		    console.log("Closed!");
		};

		var onerror = function(err) {
		    console.log("Error: " + err);
		};
		/////////////////////////////////////////////////////////////////////////
		// DEFINIMOS LAS ACCIONES PUBLICAS BASICAS DEL PACKAGE DE COMUNICACIONES
		// SEND 
		// SETCODE
		// SETONOPEN
		// SETONCLOSE
		// SETONMESSAGE
		/////////////////////////////////////////////////////////////////////////
		this.send = function(request,callback,data,headers){
			ws.send(packet(200,request,callback,data,headers));
		};
		this.setcode = function(code, action){

		};
		this.setonopen = function(action){

		};
		this.setonclose = function(action){

		};
		this.setonmessage = function(){

		};
		this.onReady = function(){

		}
		/////////////////////////////////////////////////////////////////////////
		//	DEFINIMOS EL PACKETE ESTANDARD DE COMUNICACIÓN
		/////////////////////////////////////////////////////////////////////////
		this.request = function(obj,queued){
			if(readyState)
			{	
				if((typeof obj.request != "undefined") && (typeof obj.callback != "undefined")){
					var callback = stack.addCallback(obj.callback);
					if(typeof obj.before != "undefined") {
						obj.before(obj);
					}
					if(typeof obj.done  != "undefined"){
						done.addManualCallback(callback, obj.done)
					}
					if(obj.sync)
					{
						readyState = false;
					}
					if(obj.buffered){
						var bu = buffer.query(obj.request,obj.data,obj.headers);
						if(bu){
							stack.removeCallback(callback);
							obj.callback(bu);
						}
						else
						{
							buffer.regbuffer(obj.request, obj.data, obj.headers, callback);
							ws.send(packet(obj.code,obj.request,callback,obj.data,obj.headers));
						}
					}
					else
					{
						ws.send(packet(obj.code,obj.request,callback,obj.data,obj.headers));
					}
					return true;
				}
				else
				{
					console.log("Uno de los campos requeridos no fue especificado en el metodo request del objeto de comunicaciones");
				}
			}
			else
			{
				if(typeof queued != "undefined" && queued == true){
					return false;
				}
				else
				{
					qued.queued(obj);
					return true;
				}
				
			}
		}
		var requestlog = function(obj){
			if((typeof obj.request != "undefined") && (typeof obj.callback != "undefined")){
				var callback = stack.addCallback(obj.callback);
				ws.send(packet(obj.code,obj.request,callback,obj.data,obj.headers));
			}
			else
			{
				console.log("Uno de los campos requeridos no fue especificado en el metodo request del objeto de comunicaciones");
			}
		}
		var packet = function(code,request,callback,data,headers){
			var paquete = {	
				code: (typeof code != "undefined")? code : 100,
				request: request,
				callback: callback,
				data: (typeof data != "undefined")? data : {},
				header: (typeof headers != "undefined")? headers : {} 
			};
			if(aes != null){
				var en = aes.encrypt(JSON.stringify(paquete));
				return en;
			}
			else
			{
				return JSON.stringify(paquete);
			}
		}

		/////////////////////////////////////////////////////////////////////////
		// EJECUTAMOS EL CONSTRUCTOR
		/////////////////////////////////////////////////////////////////////////
		constructor(ready);
	};
	SOCKET = new COM({
		websocket:{
			host: _CONFIG._WEBSOCKET.HOST,
			port: _CONFIG._WEBSOCKET.PORT
		}
	}, function(){
		
	});
})();