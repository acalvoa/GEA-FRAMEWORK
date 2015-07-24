package gea.controller;

import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.json.JSONException;
import org.json.JSONObject;

import gea.framework.*;
import gea.model.*;
import gea.packet.PacketErCode;
import gea.utils.Exception.Error404Exception;
import gea.utils.Exception.Error500Exception;
import gea.utils.Exception.ErrorCodeException;

public class ControllerUser extends ControllerBase{
	public static void log(Request req, Response res) throws ErrorCodeException, JSONException, Error500Exception, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		res.setCallback(req.getCallback());
		req.getSesion().generateClave(req.getData().getString("a"));
		JSONObject retorno = new JSONObject();
		res.SendCallback(retorno);
	}
}
