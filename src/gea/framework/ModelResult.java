package gea.framework;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import gea.model.ModelBase;

public class ModelResult<T extends ModelBase> {
	ArrayList<T> buffer;
	ArrayList<String> mapa = null;
	public ModelResult() {
		// TODO Auto-generated constructor stub
		buffer = new ArrayList<T>();
	}
	//DEFINIMOS EL METODO TOJSON
	public String toJSONString(){
		switch(buffer.size()){
			case 0:
				return "";
			case 1:
				JSONArray j2 = new JSONArray();
				if(mapa == null) {
					mapa = new ArrayList<String>();
					buffer.get(0).getJSON(mapa);
				}
				JSONObject j1 = new JSONObject(buffer.get(0),mapa.toArray(new String[mapa.size()]));
				j2.put(j1);
				return j2.toString();
			default:
				if(mapa == null) {
					mapa = new ArrayList<String>();
					buffer.get(0).getJSON(mapa);
				}
				j2 = new JSONArray();
				for(int i=0; i<buffer.size(); i++){
					JSONObject aux = new JSONObject(buffer.get(i),mapa.toArray(new String[mapa.size()]));
					j2.put(aux);
				}
				return j2.toString();
		}
	}
	public JSONArray toJSON(){
		switch(buffer.size()){
			case 0:
				return new JSONArray();
			case 1:
				JSONArray j2 = new JSONArray();
				if(mapa == null) {
					mapa = new ArrayList<String>();
					buffer.get(0).getJSON(mapa);
				}
				JSONObject j1 = new JSONObject(buffer.get(0),mapa.toArray(new String[mapa.size()]));
				j2.put(j1);
				return j2;
			default:
				if(mapa == null) {
					mapa = new ArrayList<String>();
					buffer.get(0).getJSON(mapa);
				}
				j2 = new JSONArray();
				for(int i=0; i<buffer.size(); i++){
					JSONObject aux = new JSONObject(buffer.get(i),mapa.toArray(new String[mapa.size()]));
					j2.put(aux);
				}
				return j2;
		}
	}
	//DEFINIMOS EL METODO DE CONTEO
	public int size(){
		return buffer.size();
	}
	//METODO JOIN
	public JSONArray Join(ModelBase p){
		return null;
	}
	public void add(T obj){
		buffer.add(obj);
	}
}
