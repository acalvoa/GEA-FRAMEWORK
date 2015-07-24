package gea.policies;

import gea.framework.Controller;
import gea.framework.Request;
import gea.utils.Exception.Error403Exception;
import gea.utils.Exception.Error404Exception;
import gea.utils.Exception.Error500Exception;
import gea.utils.Exception.ErrorCodeException;

public abstract class PoliciesBase {
	public abstract boolean policie(Request req) throws Error403Exception, Error500Exception, Error404Exception, ErrorCodeException;
}
