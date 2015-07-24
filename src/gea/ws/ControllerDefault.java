package gea.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class ControllerDefault {
	public static JSONObject response(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject retorno = new JSONObject();
		retorno.put("STATUS", "TEST");
		return retorno;
	}
}
