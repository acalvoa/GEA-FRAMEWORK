package gea.policies;

import gea.policies.PoliciesBase;
import gea.utils.Exception.Error403Exception;
import gea.utils.Exception.Error404Exception;
import gea.utils.Exception.Error500Exception;
import gea.utils.Exception.ErrorCodeException;
import gea.framework.Controller;
import gea.framework.Policies;
import gea.framework.Request;

public class HasSession extends PoliciesBase{

	@Override
	public boolean policie(Request req) throws Error403Exception, Error500Exception, Error404Exception, ErrorCodeException{
		// TODO Auto-generated method stub
		return true;
	}
}
