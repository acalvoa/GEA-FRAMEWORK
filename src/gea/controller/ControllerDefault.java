package gea.controller;

import org.json.JSONException;
import org.json.JSONObject;

import gea.framework.*;
import gea.model.*;
import gea.packet.PacketErCode;
import gea.utils.Exception.Error404Exception;
import gea.utils.Exception.ErrorCodeException;

public class ControllerDefault extends ControllerBase{
	public static void response(Request req, Response res) {
		res.er404();
	}

}
