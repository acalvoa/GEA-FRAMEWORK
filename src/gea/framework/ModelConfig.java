/**
 * 
 */
package gea.framework;

/**
 * @author acalvoa
 *
 */
public class ModelConfig {
	// DEFINIMOS LAS VARIABLES DE CONFIGURACION
	// QUE SERAN UTILIZADAS POR DEFECTO EN EL MODELO
	public boolean Find = true;
	public boolean FindOne = true;
	public boolean Update = true;
	public boolean Delete = true;
	public boolean ToJSON = true;
	public boolean Insert = true;
	public boolean Getall = true;
	public String TableName = "";
	public boolean NextVal = true;
	//METODOS AUXILIARES PARA CONFIGURAR
	public void setFind(boolean find) {
		Find = find;
	}
	public void setFindOne(boolean findOne) {
		FindOne = findOne;
	}
	public void setUpdate(boolean update) {
		Update = update;
	}
	public void setDelete(boolean delete) {
		Delete = delete;
	}
	public void setToJSON(boolean toJSON) {
		ToJSON = toJSON;
	}
	public void setTableName(String tableName) {
		TableName = tableName;
	}
	public void setInsert(boolean insert) {
		Insert = insert;
	}
	public void setGetall(boolean getall) {
		Getall = getall;
	}
	public void setNextVal(boolean nextVal) {
		NextVal = nextVal;
	}
}
