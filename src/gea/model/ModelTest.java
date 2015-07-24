package gea.model;

import java.util.ArrayList;

import gea.annotation.*;
@ModelParam(
		tableName="TBLCOMUNA",
		Find=true,
		FindOne=true,
		Update=true,
		Delete=true,
		ToJSON=true
)
public class ModelTest extends ModelBase{
	//DEFINIMOS LOS STRIBUTOS
	@ModelField
	public int COMCODIGO;
	@ModelField
	@FieldDefaultInt(defaults=20)
	public int COMPROCODIGO;
	@ModelField
	public String COMDESCRIPCION;
	/*
	 *	FUNCION DE TRANSFORMACION A TEXTO JSON HEREDA LA ESTRUCTURA COMPLETA DE CAMPOS
	 *	EN ESTE PUNTO A TRAVES DEL CODIGO REMOVE SE PUEDEN EXTRAER LOS CAMPOS QUE NO SE REQUIERAN
	 *	A LA HORA DE EXPORTAR LOS DATOS A FORMATO JSON
	 */
	@Override
	protected void toJSON(ArrayList<String> mapa){
		mapa.remove("COMDESCRIPCION");
		mapa.remove("COMPROCODIGO");
	}
}
