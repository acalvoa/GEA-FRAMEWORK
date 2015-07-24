package gea.framework;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import gea.adapters.OracleConnector;
import gea.annotation.FieldDefaultInt;
import gea.annotation.FieldDefaultString;
import gea.annotation.ModelField;
import gea.annotation.ModelFieldRequired;
import gea.annotation.ModelParam;
import gea.model.*;
import gea.types.GEOM;
import gea.utils.Exception.Error403Exception;

public class Model<T extends ModelBase> extends ModelConfig{
	Class attributes;
	ArrayList<String> fields = new ArrayList<String>();
	//DEFINIMOS EL CONSTRUCTOR
	public Model(Class c){
		this.attributes = c;
		this.config();
		this.loadField();
	}
	//DEFINIMOS EL METODO QUE SETEA LOS FIELD
	private void loadField(){
		for(Field a:attributes.getFields()){
			if(a.getAnnotation(ModelField.class) == null) 
			{
				continue;
			}
			fields.add(a.getName());
		}
	}
	// METODO PARA EXTRAER TODO EL CONTENIDO DE LA BASE DE DATOS EN TORNO AL MODELO
	public ModelResult<T> getAll() throws Error403Exception, IOException{
		if(!this.Getall) throw new Error403Exception("Operacion no permitida en modelo de datos");
		ModelResult<T> retorno = new ModelResult<T>();
		try{
			StringBuilder query = new StringBuilder("SELECT ");
			for(int i=0; i<fields.size(); i++){
				Field f = attributes.getField(fields.get(i));
				if(f.getType().isPrimitive() || f.getType() == String.class)
				{
					query.append(fields.get(i)+",");
				}
				else{
					try{
						
						Method method = f.getType().getMethod("GetSQL", String.class);
						query.append(method.invoke(null, fields.get(i)));
					}
					catch(Exception e){
						new Error403Exception("Error al convertir al objeto del modelo.\n" + e.getMessage());
					}
				}
			}
			query.deleteCharAt(query.length()-1);
			query.append(" FROM "+this.TableName);
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			ResultSet res = ora.query(query.toString());
			if(res != null){
				while(res.next()){
					T aux = (T)attributes.newInstance();
					for(String g:fields){
						Field f = attributes.getField(g);
						if(f.getType() == int.class){
							aux.getClass().getField(g).set(aux, res.getInt(g));
						}
						else if(f.getType() == double.class){
							aux.getClass().getField(g).set(aux, res.getDouble(g));
						}
						else if(f.getType() == long.class){
							aux.getClass().getField(g).set(aux, res.getLong(g));
						}
						else if(f.getType() == String.class){
							aux.getClass().getField(g).set(aux, res.getString(g));
						}
						else{
							try{
								aux.getClass().getField(g).set(aux, f.getType().getConstructor(String.class).newInstance(res.getString(g)));
							}
							catch(Exception e)
							{
								new Error403Exception("Error al convertir al objeto del modelo.\n" + e.getMessage());
							}
						}
					}
					retorno.add(aux);
				}
			}
			ora.close();
		}
		catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return retorno;
	}
	// METODO PARA EXTRAER TODO EL CONTENIDO DE LA BASE DE DATOS EN TORNO AL MODELO
		public ModelResult<T> getAll(int limit) throws Error403Exception, IOException{
			if(!this.Getall) throw new Error403Exception("Operacion no permitida en modelo de datos");
			ModelResult<T> retorno = new ModelResult<T>();
			try{
				StringBuilder query = new StringBuilder("SELECT ");
				StringBuilder limite = new StringBuilder("SELECT ");
				for(int i=0; i<fields.size(); i++){
					Field f = attributes.getField(fields.get(i));
					limite.append(fields.get(i)+",");
					if(f.getType().isPrimitive() || f.getType() == String.class)
					{
						query.append(fields.get(i)+",");
					}
					else{
						try{
							
							Method method = f.getType().getMethod("GetSQL", String.class);
							query.append(method.invoke(null, fields.get(i)));
						}
						catch(Exception e){
							new Error403Exception("Error al convertir al objeto del modelo. \n"+e.getMessage());
						}
					}
				}
				query.deleteCharAt(query.length()-1);
				limite.deleteCharAt(limite.length()-1);
				query.append(" FROM "+this.TableName);
				limite.append(" FROM ("+query.toString()+") where ROWNUM <= "+limit);
				query = limite;
				OracleConnector ora;
				ora = new OracleConnector();
				ora.connect();
				ResultSet res = ora.query(query.toString());
				if(res != null){
					while(res.next()){
						T aux = (T)attributes.newInstance();
						for(String g:fields){
							Field f = attributes.getField(g);
							if(f.getType() == int.class){
								aux.getClass().getField(g).set(aux, res.getInt(g));
							}
							else if(f.getType() == double.class){
								aux.getClass().getField(g).set(aux, res.getDouble(g));
							}
							else if(f.getType() == long.class){
								aux.getClass().getField(g).set(aux, res.getLong(g));
							}
							else if(f.getType() == String.class){
								aux.getClass().getField(g).set(aux, res.getString(g));
							}
							else{
								try{
									aux.getClass().getField(g).set(aux, f.getType().getConstructor(String.class).newInstance(res.getString(g)));
								}
								catch(Exception e)
								{
									new Error403Exception("Error al convertir al objeto del modelo.\n"+e.getMessage());
								}
							}
						}
						retorno.add(aux);
					}
				}
				ora.close();
			}
			catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return retorno;
		}
	// DEFINIMOS EL METODO FIND
	public ModelResult<T> find(String p) throws Error403Exception, IOException{
		if(!this.Find) throw new Error403Exception("Operacion no permitida en modelo de datos");
		ModelResult<T> retorno = new ModelResult<T>();
		try{
			JSONObject s = new JSONObject(p);
			int limit = -1;
			if(s.has("LIMIT")){
				limit = s.getInt("LIMIT");
				s.remove("LIMIT");
			}
			StringBuilder query = new StringBuilder("SELECT ");
			StringBuilder limite = new StringBuilder("SELECT ");
			for(int i=0; i<fields.size(); i++){
				Field f = attributes.getField(fields.get(i));
				limite.append(fields.get(i)+",");
				if(f.getType().isPrimitive() || f.getType() == String.class)
				{
					query.append(fields.get(i)+",");
				}
				else{
					try{
						
						Method method = f.getType().getMethod("GetSQL", String.class);
						query.append(method.invoke(null, fields.get(i)));
					}
					catch(Exception e){
						new Error403Exception("Error al convertir al objeto del modelo.\n"+e.getMessage());
					}
				}
			}
			query.deleteCharAt(query.length()-1);
			limite.deleteCharAt(limite.length()-1);
			if(limit == -1){
				query.append(" FROM "+this.TableName+" "+this.wheretxt(s));
			}
			else
			{
				
				query.append(" FROM "+this.TableName+" "+this.wheretxt(s));
				limite.append(" FROM ("+query.toString()+") where ROWNUM <= "+limit);
				query = limite;
			}
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			ResultSet res = ora.query(query.toString());
			if(res != null){
				while(res.next()){
					T aux = (T)attributes.newInstance();
					for(String g:fields){
						Field f = attributes.getField(g);
						if(f.getType() == int.class){
							aux.getClass().getField(g).set(aux, res.getInt(g));
						}
						else if(f.getType() == double.class){
							aux.getClass().getField(g).set(aux, res.getDouble(g));
						}
						else if(f.getType() == long.class){
							aux.getClass().getField(g).set(aux, res.getLong(g));
						}
						else if(f.getType() == String.class){
							aux.getClass().getField(g).set(aux, res.getString(g));
						}
						else{
							try{
								aux.getClass().getField(g).set(aux, f.getType().getConstructor(String.class).newInstance(res.getString(g)));
							}
							catch(Exception e)
							{
								new Error403Exception("Error al convertir al objeto del modelo.\n"+e.getMessage());
							}
						}
					}
					retorno.add(aux);
				}
			}
			ora.close();
		}
		catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return retorno;
	}
	//DEFINIMOS EL MOTOD MATCH PARA GENERAR CAMPOS POR PATRON
	// DEFINIMOS EL METODO FIND
		public ModelResult<T> match(String p) throws Error403Exception, IOException{
			if(!this.Find) throw new Error403Exception("Operacion no permitida en modelo de datos");
			ModelResult<T> retorno = new ModelResult<T>();
			try{
				JSONObject s = new JSONObject(p);
				int limit = -1;
				if(s.has("LIMIT")){
					limit = s.getInt("LIMIT");
					s.remove("LIMIT");
				}
				StringBuilder query = new StringBuilder("SELECT ");
				StringBuilder limite = new StringBuilder("SELECT ");
				for(int i=0; i<fields.size(); i++){
					Field f = attributes.getField(fields.get(i));
					limite.append(fields.get(i)+",");
					if(f.getType().isPrimitive() || f.getType() == String.class)
					{
						query.append(fields.get(i)+",");
					}
					else{
						try{
							
							Method method = f.getType().getMethod("GetSQL", String.class);
							query.append(method.invoke(null, fields.get(i)));
						}
						catch(Exception e){
							new Error403Exception("Error al convertir al objeto del modelo.\n"+e.getMessage());
						}
					}
				}
				query.deleteCharAt(query.length()-1);
				limite.deleteCharAt(limite.length()-1);
				if(limit == -1){
					query.append(" FROM "+this.TableName+" "+this.matchtxt(s));
				}
				else
				{
					
					query.append(" FROM "+this.TableName+" "+this.matchtxt(s));
					limite.append(" FROM ("+query.toString()+") where ROWNUM <= "+limit);
					query = limite;
				}
				OracleConnector ora;
				ora = new OracleConnector();
				ora.connect();
				ResultSet res = ora.query(query.toString());
				if(res != null){
					while(res.next()){
						T aux = (T)attributes.newInstance();
						for(String g:fields){
							Field f = attributes.getField(g);
							if(f.getType() == int.class){
								aux.getClass().getField(g).set(aux, res.getInt(g));
							}
							else if(f.getType() == double.class){
								aux.getClass().getField(g).set(aux, res.getDouble(g));
							}
							else if(f.getType() == long.class){
								aux.getClass().getField(g).set(aux, res.getLong(g));
							}
							else if(f.getType() == String.class){
								aux.getClass().getField(g).set(aux, res.getString(g));
							}
							else{
								try{
									aux.getClass().getField(g).set(aux, f.getType().getConstructor(String.class).newInstance(res.getString(g)));
								}
								catch(Exception e)
								{
									new Error403Exception("Error al convertir al objeto del modelo.\n"+e.getMessage());
								}
							}
						}
						retorno.add(aux);
					}
				}
				ora.close();
			}
			catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return retorno;
		}
	//DEFINIMOS UN METODO PARA EXTRACCION DE LOS CAMPOS WHERE
	private String wheretxt(JSONObject json) throws SecurityException, NoSuchFieldException{
		StringBuilder query = new StringBuilder("WHERE ");
		Iterator i = json.keys();
		while(i.hasNext()){
			String llave = i.next().toString();
			try
			{
				JSONArray a = json.getJSONArray(llave);
				query.append(campoB(llave,json, a, true));
			}
			catch(Exception e){
				query.append(campoB(llave,json, null, false));
			}
			if(i.hasNext())
			{
				query.append(" AND ");
			}
		}
		return query.toString();
	}
	//DEFINIMOS UN METODO PARA EXTRACCION DE LOS CAMPOS WHERE
	private String matchtxt(JSONObject json) throws SecurityException, NoSuchFieldException{
		StringBuilder query = new StringBuilder("WHERE ");
		Iterator i = json.keys();
		while(i.hasNext()){
			String llave = i.next().toString();
			try
			{
				JSONArray a = json.getJSONArray(llave);
				query.append(campoBmatch(llave,json, a, true));
			}
			catch(Exception e){
				query.append(campoBmatch(llave,json, null, false));
			}
			if(i.hasNext())
			{
				query.append(" AND ");
			}
		}
		return query.toString();
	}
	//METODO PARA ANALISIS DE LOS CAMPOS A INGRESAR EN WHERE
	private String campoB(String llave, JSONObject json, JSONArray jsona, boolean isarray) throws SecurityException, NoSuchFieldException
	{
		StringBuilder query = new StringBuilder();
		if(fields.indexOf(llave) >= 0){
			Field f = attributes.getField(llave);
			if(f.getType() == int.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							query.append(this.TableName+"."+llave+"="+jsona.getInt(i));
						}
						query.append(")");
					}
					else
					{
						query.append(this.TableName+"."+llave+"="+json.getInt(llave));
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
			else if(f.getType() == double.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							query.append(this.TableName+"."+llave+"="+jsona.getDouble(i));
						}
						query.append(")");
					}
					else
					{
						query.append(this.TableName+"."+llave+"="+json.getDouble(llave));
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
			else if(f.getType() == String.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							query.append(this.TableName+"."+llave+"='"+getSqlRealScapeString(jsona.getString(i))+"'");
						}
						query.append(")");
					}
					else
					{
						query.append(this.TableName+"."+llave+"='"+getSqlRealScapeString(json.getString(llave))+"'");
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
		}
		return query.toString();
	}
	private String campoBmatch(String llave, JSONObject json, JSONArray jsona, boolean isarray) throws SecurityException, NoSuchFieldException
	{
		StringBuilder query = new StringBuilder();
		if(fields.indexOf(llave) >= 0){
			Field f = attributes.getField(llave);
			if(f.getType() == int.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
								query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" "+json.getString(llave).substring(3));
							}
							else
							{
								query.append(this.TableName+"."+llave+" LIKE "+json.getString(llave));
							}
						}
						query.append(")");
					}
					else
					{
						if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
							query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" "+json.getString(llave).substring(3));
						}
						else
						{
							query.append(this.TableName+"."+llave+" LIKE "+json.getString(llave));
						}
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
			else if(f.getType() == double.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
								query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" "+json.getString(llave).substring(3));
							}
							else
							{
								query.append(this.TableName+"."+llave+" LIKE "+json.getString(llave));
							}
						}
						query.append(")");
					}
					else
					{
						if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
							query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" "+json.getString(llave).substring(3));
						}
						else
						{
							query.append(this.TableName+"."+llave+" LIKE "+json.getString(llave));
						}
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
			else if(f.getType() == double.class || f.getType() == long.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
								query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" "+json.getString(llave).substring(3));
							}
							else
							{
								query.append(this.TableName+"."+llave+" LIKE "+json.getString(llave));
							}
						}
						query.append(")");
					}
					else
					{
						if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
							query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" "+json.getString(llave).substring(3));
						}
						else
						{
							query.append(this.TableName+"."+llave+" LIKE "+json.getString(llave));
						}
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
			else if(f.getType() == String.class){
				try{
					if(isarray){
						query.append("(");
						for(int i=0; i< jsona.length(); i++){
							if(i!=0){ 
								query.append(" OR "); 
							}
							if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
								query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" '"+getSqlRealScapeString(json.getString(llave).substring(3))+"'");
							}
							else
							{
								query.append("UPPER("+this.TableName+"."+llave+") LIKE '%"+getSqlRealScapeString(json.getString(llave).toUpperCase())+"%'");
							}
						}
						query.append(")");
					}
					else
					{
						if(json.getString(llave).indexOf("==") == 0 || json.getString(llave).indexOf(">=") == 0 || json.getString(llave).indexOf("<=") == 0 || json.getString(llave).indexOf(">>") == 0 || json.getString(llave).indexOf("<<") == 0 || json.getString(llave).indexOf("!=") == 0){
							query.append(this.TableName+"."+llave+" "+re_operator(json.getString(llave).substring(0, 2))+" '"+getSqlRealScapeString(json.getString(llave).substring(3))+"'");
						}
						else
						{
							query.append("UPPER("+this.TableName+"."+llave+") LIKE '%"+getSqlRealScapeString(json.getString(llave).toUpperCase())+"%'");
						}
					}
				}
				catch(Exception e){
					System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
					return null;
				}
			}
		}
		return query.toString();
	}
	// DEFINIMOS EL METODO FINDONE
	public ModelResult<T> findOne(String p) throws InstantiationException, IllegalAccessException, Error403Exception, IOException{
		if(!this.FindOne) throw new Error403Exception("Operacion no permitida en modelo de datos");
		ModelResult<T> retorno = new ModelResult<T>();
		try{
			JSONObject s = new JSONObject(p);
			int limit = -1;
			if(s.has("LIMIT")){
				limit = s.getInt("LIMIT");
				s.remove("LIMIT");
			}
			StringBuilder query = new StringBuilder("SELECT ");
			StringBuilder limite = new StringBuilder("SELECT ");
			for(int i=0; i<fields.size(); i++){
				Field f = attributes.getField(fields.get(i));
				limite.append(fields.get(i)+",");
				if(f.getType().isPrimitive() || f.getType() == String.class)
				{
					query.append(fields.get(i)+",");
				}
				else{
					try{
						Method method = f.getType().getMethod("GetSQL", String.class);
						query.append(method.invoke(null, fields.get(i)));
					}
					catch(Exception e){
						new Error403Exception("Error al convertir al objeto del modelo.");
					}
				}
			}
			query.deleteCharAt(query.length()-1);
			limite.deleteCharAt(limite.length()-1);
			if(limit == -1){
				query.append(" FROM "+this.TableName+" "+this.wheretxt(s));
			}
			else
			{
				
				query.append(" FROM "+this.TableName+" "+this.wheretxt(s));
				limite.append(" FROM ("+query.toString()+") where ROWNUM <= "+limit);
				query = limite;
			}
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			ResultSet res = ora.query(query.toString());
			if(res != null){
				if(res.next()){
					T aux = (T)attributes.newInstance();
					for(String g:fields){
						Field f = attributes.getField(g);
						if(f.getType() == int.class){
							aux.getClass().getField(g).set(aux, res.getInt(g));
						}
						else if(f.getType() == double.class){
							aux.getClass().getField(g).set(aux, res.getDouble(g));
						}
						else if(f.getType() == long.class){
							aux.getClass().getField(g).set(aux, res.getLong(g));
						}
						else if(f.getType() == String.class){
							aux.getClass().getField(g).set(aux, res.getString(g));
						}
						else{
							try{
								aux.getClass().getField(g).set(aux, f.getType().getConstructor(String.class).newInstance(res.getString(g)));
							}
							catch(Exception e)
							{
								new Error403Exception("Error al convertir al objeto del modelo.");
							}
						}
					}
					retorno.add(aux);
				}
			}
			ora.close();
		}
		catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return retorno;
	}
	//DEFINIMOS EL METODO UPDATE
	public boolean update(String p, String w) throws Error403Exception, IOException{
		if(!this.Update) throw new Error403Exception("Operacion no permitida en modelo de datos");
		try{
			JSONObject s = new JSONObject(p);
			JSONObject we = new JSONObject(w);
			StringBuilder query = new StringBuilder("UPDATE "+this.TableName+" SET ");
			Iterator i = s.keys();
			while(i.hasNext()){
				String llave = i.next().toString();
				try
				{
					if(fields.indexOf(llave) >= 0){
						Field f = attributes.getField(llave);
						if(f.getType() == int.class){
							try{
								query.append(this.TableName+"."+llave+"="+s.getInt(llave));
							}
							catch(Exception e){
								System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
								return false;
							}
						}
						else if(f.getType() == double.class){
							try{
								query.append(this.TableName+"."+llave+"="+s.getDouble(llave));
							}
							catch(Exception e){
								System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
								return false;
							}
						}
						else if(f.getType() == long.class){
							try{
								query.append(this.TableName+"."+llave+"="+s.getLong(llave));
							}
							catch(Exception e){
								System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
								return false;
							}
						}
						else if(f.getType() == String.class){
							try{
								query.append(this.TableName+"."+llave+"='"+s.getString(llave)+"'");
							}
							catch(Exception e){
								System.out.println("Campo de busqueda "+llave+" con tipo incorrecto en contraste con modelo definido.");
								return false;
							}
						}
					}
				}
				catch(Exception e){
					return false;
				}
				if(i.hasNext())
				{
					query.append(", ");
				}
			}
			query.append(" "+this.wheretxt(we));
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			if(ora.update(query.toString()))
			{
				ora.closeOP();
				return true;
			}
			else
			{
				ora.closeOP();
				return false;
			}
		}
		catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}
	// DEFINIMOS EL METODO DELETE
	public boolean delete(String p) throws Error403Exception, IOException{
		if(!this.Delete) throw new Error403Exception("Operacion no permitida en modelo de datos");
		try{
			JSONObject s = new JSONObject(p);
			StringBuilder query = new StringBuilder("DELETE FROM "+this.TableName+" "+this.wheretxt(s));
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			if(ora.delete(query.toString()))
			{
				ora.closeOP();
				return true;
			}
			else
			{
				ora.close();
				return false;
			}			
		}
		catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}
	//DEFINIMOS EL METODO INSERT
	public boolean insert(String p) throws Error403Exception, IOException{
		if(!this.Insert) throw new Error403Exception("Operacion no permitida en modelo de datos");
		try{
			JSONObject s = new JSONObject(p);
			
			if(s.length() == 0){
				System.out.println("No se puede ejecutar una operación INSERT vacia sobre este modelo");
				return false;
			}
			StringBuilder query = new StringBuilder("INSERT INTO "+this.TableName+" (");
			ArrayList<Lfield> valores = new ArrayList<Lfield>();
			StringBuilder values = new StringBuilder(" VALUES (");
			boolean valid = false;
			if(fields.size() >= 0){
				for(int i=0; i<fields.size(); i++){
					boolean data = false;
					try
					{
						Field f = attributes.getField(fields.get(i));
						if(f.getType() == int.class){
							try{
								if(i > 0) {
									values.append(",");
									query.append(",");
								}
								values.append("?");
								valores.add(new Lfield("INT",i,s.getInt(fields.get(i))));
								query.append(this.TableName+"."+fields.get(i));
								data = true;
							}
							catch(Exception e){
								if(f.getAnnotation(FieldDefaultInt.class) != null){
									valores.add(new Lfield("INT",i,f.getAnnotation(FieldDefaultInt.class).defaults()));
									query.append(this.TableName+"."+fields.get(i));
									data = true;
								}
								else{
									if(f.getAnnotation(ModelFieldRequired.class) != null){
										System.out.println("El campo "+fields.get(i)+" es requerido para hacer uan inserción.");
										return false;
									}
								}
							}
						}
						else if(f.getType() == double.class){
							try{
								if(i > 0) {
									values.append(",");
									query.append(",");
								}
								valores.add(new Lfield("DOUBLE",i,s.getDouble(fields.get(i))));
								query.append(this.TableName+"."+fields.get(i));
								data = true;
							}
							catch(Exception e){
								if(f.getAnnotation(FieldDefaultInt.class) != null){
									valores.add(new Lfield("DOUBLE",i,f.getAnnotation(FieldDefaultInt.class).defaults()));
									query.append(this.TableName+"."+fields.get(i));
									data = true;
								}
								else{
									if(f.getAnnotation(ModelFieldRequired.class) != null){
										System.out.println("El campo "+fields.get(i)+" es requerido para hacer uan inserción.");
										return false;
									}
								}
							}
						}
						else if(f.getType() == long.class){
							try{
								if(i > 0) {
									values.append(",");
									query.append(",");
								}
								valores.add(new Lfield("LONG",i,s.getLong(fields.get(i))));
								query.append(this.TableName+"."+fields.get(i));
								data = true;
							}
							catch(Exception e){
								if(f.getAnnotation(FieldDefaultInt.class) != null){
									valores.add(new Lfield("LONG",i,f.getAnnotation(FieldDefaultInt.class).defaults()));
									query.append(this.TableName+"."+fields.get(i));
									data = true;
								}
								else{
									if(f.getAnnotation(ModelFieldRequired.class) != null){
										System.out.println("El campo "+fields.get(i)+" es requerido para hacer uan inserción.");
										return false;
									}
								}
							}
						}
						else if(f.getType() == String.class){
							try{
								if(i > 0) {
									values.append(",");
									query.append(",");
								}
								values.append("'"+s.getString(fields.get(i))+"'");
								query.append(this.TableName+"."+fields.get(i));
								data = true;
							}
							catch(Exception e){
								if(f.getAnnotation(FieldDefaultInt.class) != null){
									values.append("'"+f.getAnnotation(FieldDefaultString.class).defaults()+"'");
									query.append(this.TableName+"."+fields.get(i));
									data = true;
								}
								else{
									if(f.getAnnotation(ModelFieldRequired.class) != null){
										System.out.println("El campo "+fields.get(i)+" es requerido para hacer uan inserción.");
										return false;
									}
								}
							}
						}
						else
						{
							try{
								if(i > 0) {
									values.append(",");
									query.append(",");
								}
								values.append(s.getString(fields.get(i)));
								query.append(this.TableName+"."+fields.get(i));
								data = true;
							}
							catch(Exception e){
								if(f.getAnnotation(FieldDefaultInt.class) != null){
									values.append("'"+f.getAnnotation(FieldDefaultString.class).defaults()+"'");
									query.append(this.TableName+"."+fields.get(i));
									data = true;
								}
								else{
									if(f.getAnnotation(ModelFieldRequired.class) != null){
										System.out.println("El campo "+fields.get(i)+" es requerido para hacer uan inserción.");
										return false;
									}
								}
							}
						}
					}
					catch(Exception e){
						return false;
					}
					if(i < (fields.size()) && data)
					{
						query.append(",");
						values.append(",");
					}
					if(i == (fields.size()-1))
					{
						query.deleteCharAt(query.length()-1);
						values.deleteCharAt(values.length()-1);
					}
				}
			}
			else
			{
				System.out.println("El modelo especificado no contiene campos");
				return false;
			}
			query.append(")");
			values.append(")");
			query.append(values);
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			if(ora.update(query.toString()))
			{
				ora.closeOP();
				return true;
			}
			else
			{
				ora.closeOP();
				return false;
			}
		}
		catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}
	
	
	// DEFINIMOS EL METODO PARA OBTENER EL NEXTVAL DE UN SEQUENCE
	public int getNextVal(String sequence) throws IllegalAccessException, Error403Exception, IOException{
		int sequencia = -1;
		try{
			
			StringBuilder query = new StringBuilder("SELECT ");
			query.append(sequence);
			query.append(".NEXTVAL FROM DUAL");
			OracleConnector ora;
			ora = new OracleConnector();
			ora.connect();
			ResultSet res = ora.query(query.toString());
			if(res != null){
				res.next();
				sequencia = res.getInt(1);
			}
			ora.close();
		}catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return sequencia;
	}
		
	//DEFINIMOS EL METODO DE CONFIGURACION DEL MODELO
	//DESDE ACA PODEMOS CONFIGURAR EXCEPCIONES EN CASO DE NO EXISTIR COINCIDENCIA
	//EN LAS ANOTACIONES DE CONFIGURACIÓN
	private void config(){
		ModelParam m = (ModelParam) attributes.getAnnotation(ModelParam.class);
		//DEFINIMOS LOS PARAMETROS DE CONFIGURACION
		//ESTOS DEBEN SER DEFINIDOS EN EL MODELO
		this.setTableName(m.tableName());
		this.setFind(m.Find());
		this.setFindOne(m.FindOne());
		this.setDelete(m.Delete());
		this.setUpdate(m.Update());
		this.setNextVal(m.NextVal());
		this.setToJSON(m.ToJSON());
	}
	private String re_operator(String ope){
		if(ope.equals(">>")){
			return ">";
		}
		else if(ope.equals("<<")){
			return "<";
		}
		else if(ope.equals("!=")){
			return "!=";
		}
		else if(ope.equals(">=")){
			return ">=";
		}
		else if(ope.equals("<=")){
			return "<=";
		}
		else if(ope.equals("==")){
			return "=";
		}
		return ope;
	}
	public String getSqlRealScapeString(String str) {
		String data = null;
		if (str != null && str.length() > 0) {String
			str = str.replace("\\", "\\\\");
		    str = str.replace("'", "''");
		    str = str.replace("\0", "\\0");
		    str = str.replace("\n", "\\n");
		    str = str.replace("\r", "\\r");
		    str = str.replace("\"", "\\\"");
		    str = str.replace("\\x1a", "\\Z");
		    data = str;
		 }
		 return data;
	}
}
class Lfield{
	public String type;
	public int index;
	public Object value;
	public Lfield(String type, int index, String key) {
		this.type = type;
		this.index = index;
		this.value = (Object)key;
	}
	public Lfield(String type, int index, Integer key) {
		this.type = type;
		this.index = index;
		this.value = (Object)key;
	}
	public Lfield(String type, int index, Double key) {
		this.type = type;
		this.index = index;
		this.value = (Object)key;
	}
	public Lfield(String type, int index, Long key) {
		this.type = type;
		this.index = index;
		this.value = (Object)key;
	}
	public Lfield(String type, int index, Object key) {
		this.type = type;
		this.index = index;
		this.value = key;
	}
}