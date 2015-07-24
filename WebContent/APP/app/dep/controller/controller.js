// APP BEGIN
(function(){
	CONTROLLER = function(args){
		// DEFINIMOS LOS OBJETOS DONDE SE ALMACENAN LOS CONTROLADORES
		var modules = {};
		var api;
		//DEFINIMOS EL CONSTRUCTOR
		var construct = function(args){
			getControllers();
		};
		//DEFINMOS EL CONTROLADOR DE LA APLICACION
		this.extend = function(controller, func){
			if(typeof modules[controller] == "undefined"){
				modules[controller] = {};
				modules[controller]["controller"] = new func(api);
			}
			else
			{
				modules[controller]["controller"] = new func(api);
			}
		};
		// BUSCAMOS MAPA DE CONTROLADORES PRESENTES EN EL DOCUMENTO HTML
		var getControllers = function(){
			// DEFINIMOS EL MICROMODELO
			var mmodel = function(self, c){
				//DEFINIMOS LAS VARIABLES DEL MMODEL
				var c = c;
				var s = this;
				var that = self;
				var defaults = null;
				var element = that[0].tagName;
				var ifirst = false;
				var type = null;
				var required = false;
				var value;
				//DEFINIMOS EL CONSTRUCTOR DEL MMODEL
				var construct = function(obj){			
					if(typeof obj.attr("gea-default") != "undefined"){
						defaults = obj.attr("gea-default");
						obj.removeAttr("gea-default");
					}
					if(typeof obj.attr("gea-type") != "undefined"){
						type = obj.attr("gea-type");
						obj.removeAttr("gea-type");
					}
					if(typeof obj.attr("gea-ifirst") != "undefined"){
						ifirst = true;
						obj.removeAttr("ifirst");
					}
					if(typeof obj.attr("gea-required") != "undefined"){
						required = true;
						obj.removeAttr("gea-required");
					}
					s.val();
				};
				this.val = function(){
					value = that.val();
					if(defaults != null && value == ""){
						value = defaults;
					}
					else
					{
						if(element == "INPUT" || element == "SELECT"){
							value = that.val();
						}
						else{
							value = that.html();
						}
					}
				};
				this.toString = function(){ 
					return validate();
				};
				var validate = function(){
					if((required && value == "") || (element == "SELECT" && required && ifirst && that.children('option[value="'+value+'"]').index() == 0)){
						if(defaults != null){
							return defaults;
						}
						else
						{
							if(typeof c.controller.required != "undefined"){
								throw new c.controller.required(that);
							}
							else
							{
								console.log("El campo "+self.attr("name")+" es requerido para ejecutar la accion.");
							}
						}
					}
					else
					{
						if(type == null){
							return value;
						}
						else
						{
							if(typeof value == type){
								return value;
							}
							else
							{
								if(typeof c.controller.typeerror != "undefined"){
									throw c.controller.typeerror(that);
								}
								else
								{
									console.log("El tipo para el campo "+self.attr("name")+" es incorrecto.");
								}
							}
						}
					}
				};
				construct(self);
			};
			//DEFINIMOS EL CONTROLADOR
			var module = function(controller, obj){
				//DEFINIMOS LAS VARIABLES A UTILIZAR
				// C DEFINE AL OBJETO MODULO
				var c = controller;
				var controlador = obj;
				var that = this;
				var events = {};
				var models = {};
				// DEFINIMOS EL CONSTRUCTOR
				var construct = function(){
					search_fields();
					search_event();
				};
				var getfield  = function(text){
					try{
						var retorno = {};
						$.each(models[text], function(key,value){
							retorno[key] = value.toString();
						});
						return retorno;
					}catch(e){

					}
					
				};
				// BUSCAMOS LOS FIELDS DEL CONTROLADOR
				var search_fields = function(){
					controlador.children("input[gea-field], select[gea-field], div[gea-field]").each(function(){
						var self = $(this);
						if(self.attr("name") != "undefined"){
							if(typeof models[self.attr("gea-field")] == "undefined")
							{
								models[self.attr("gea-field")] = {};
							}
							models[self.attr("gea-field")][self.attr("name")] = new mmodel(self, c);
							self.on('change', function(){
								models[self.attr("gea-field")][self.attr("name")].val();
							});
						}
						else
						{
							console.log("Campo del micromodelo "+self.attr("gea-field")+" sin nombre definido.");
						}
					});
				};
				// BUSCAMOS LOS EVENTOS PARA EJECUTAR ORDENES
				var search_event = function(){
					controlador.children("*[gea-event]").each(function(){
						$(this).on($(this).attr("gea-event"), function(){
							if(typeof c.controller[$(this).attr("gea-action")] != "undefined"){
								c.controller[$(this).attr("gea-action")](getfield($(this).attr("gea-action")));
							}
							else{
								console.log("No existe la accion "+$(this).attr("gea-action")+" dentro del controlador "+controlador.attr("gea-controller"));
							}
						});
					});
				};
				// EJECUTAMOS EL CONSTRUCTOR DEL CONTROLADOR
				construct();
			};
			//BUSCAMOS LAS SENTENCIAS DE CONTROLADOR
			$("*[gea-controller]").each(function(){
				var controlador = $(this);
				modules[$(this).attr("gea-controller")] = {};
				modules[$(this).attr("gea-controller")]["controller"] = null;
				modules[$(this).attr("gea-controller")]["module"] = new module(modules[$(this).attr("gea-controller")], controlador);
			});	
		};
		//EJECUTAMOS EL CONSTRUCTOR
		construct(args);
	};

})();