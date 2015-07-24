// APP BEGIN
(function(){
	// DEFINIMOS LA VARIABLES DE ENTORNO
	//Y SUBRUTINAS DEL PAQUETE
	MODEL = function(){
		var model =  function(){
			//DEFINIMOS LA VARIABLE QUE CONTIENE EL MODELO ORM
			var data = null;
		};
		var controller = function(){
			//DEFINIMOS LAS OPERACIONES
			this.update = function(o){

			};
			this.find = function(o){	

			};
			this.findOne = function(o){

			};
			this.Delete = function(o){

			};
			this.toJSON = function(){

			};
			this.create = function(o){

			}
		};
		//DEFINIMOS EL METODO DE EXTENSION
		//CONTENEMOS LOS METODOS DE MODELO, CONTROLADOR, MODULO Y ERROR
		this.extend = function(NAME,OBJ){
			$.extend(OBJ, new model);
			$.extend(OBJ, new controller);
			$.extend(OBJ, new modulos);
			$.extend(OBJ, new error);
			OBJ.constructor();
			//LOS VOLVEMOS UNA VARIABLE GLOBAL QUE SE PUEDA IMPLEMENTAR
			$[NAME] = OBJ;
		};
		//NECESITAMOS LOS METODOS DE COMPROBACION DE DATOS
		var modulos = function(){
			config = null;
			var type = {
				"string": true,
				"double": true,
				"object": true,
				"int": true,
				"boolean": true,
				"spatial": true
			};
			this.constructor = function(){
				loadConfig();
				loadPage();
				checkParams(this);
			};
			checkParams = function(t){
				if(typeof t.tableName == "undefined"){
					error.e500();
					return;
				}
				if(typeof t.data == "undefined"){
					error.e500();
					return;
				}
				else
				{
					$.each(t.data, function(key,value){
						switch(typeof value){
							case "string":
								if(typeof type[value] == "undefined") 
								{
									error.e500();
									return;
								}
								break;
							case "object":
								if(typeof type[value.type] == "undefined") 
								{
									error.e500();
									return;
								}
								break;
						}
					});
				}
			};
			verifyData = function(){

			};
			var loadConfig = function(){
				$.ajax({
					url: "app/config.json",
					async: false,
					dataType: "json",
					success: function(resultado){
						config = resultado;
						console.log("Config loaded");
					}
				}).error(function(){
					console.log("Config error");
				});
			};
		};
		//DEFINIMOS LA CLASE QUE DEFINE Y MANEJA LOS ERRORES A PARTIR DE LOS TEMPLATES
		var error = function(){
			var handler = ["404","500"];
			this.error = {};
			var errors = {};
			loadPage = function(){
				$.each(handler, function(key,value){
					$.ajax({
						url: config.asset.layout+value+".html", 
						async: false,
						success: function(resultado){ 
							errors["b"+value] = resultado;
							error["e"+value] = function(){
								document.write(errors["b"+value]);
							}
						}
					});
				});
			};
		};
	};
})();
