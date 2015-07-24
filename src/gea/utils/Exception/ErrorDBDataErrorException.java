package gea.utils.Exception;

import org.json.JSONObject;

public class ErrorDBDataErrorException extends BaseException{

	private static final long serialVersionUID = 1L;
	private static final String code = "CODE DB02";

	public ErrorDBDataErrorException() {
        super();
    }

    public ErrorDBDataErrorException(String message) {
        super(message);
    }
    
    public ErrorDBDataErrorException(String message, JSONObject reg) {
    	
        super(message);
    }

    public ErrorDBDataErrorException(Throwable exception) {
        super(exception);
    }

}
