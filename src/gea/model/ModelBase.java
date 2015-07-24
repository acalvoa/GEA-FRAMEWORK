package gea.model;

import gea.annotation.ModelField;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;

public class ModelBase {
	//ESTA CLASE SE UTILIZA PARA QUE LAS CLASES MODELOS CONSERVEN IDENTIDAD
	//Y PUEDAN SER RECONOCIDAS POR EL CARGADOR AUTOMATICO DE MODELOS
	//ES IMPORTANTE QUE CADA VEZ QUE SE CREE UN MODELO SE HEREDE ESTA CLASE
	//DE LO CONTRARIO EL MODELO SERA IGNORADO POR EL LOADER
	public final void getJSON(ArrayList<String> mapa){
		Field p[] = this.getClass().getFields();
		for(int i=0; i< p.length; i++){
			if(p[i].getAnnotation(ModelField.class) != null){
				mapa.add(p[i].getName());
			}
		}
		this.toJSON(mapa);
	}
	protected void toJSON(ArrayList<String> mapa){
	}
	public String toString(){
		Field[] f = this.getClass().getFields();
		StringBuilder str = new StringBuilder();
		for(int j=0; j<f.length; j++){
			try {
				str.append(f[j].getName()+":"+f[j].get(this)+", ");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				System.out.println("Error en toString "+ e.toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				System.out.println("Error en toString "+ e.toString());
			}
		}
		return str.toString();
	}
}
