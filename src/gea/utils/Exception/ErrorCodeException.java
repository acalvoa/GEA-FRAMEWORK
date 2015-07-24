package gea.utils.Exception;

public class ErrorCodeException extends BaseException{
	
	private static final long serialVersionUID = 1L;
	private int code;

	public ErrorCodeException() {
        super();
    }

    public ErrorCodeException(String message) {
        super(message);
    }
    public ErrorCodeException(int code, String message) {
    	super(message);
    	this.code = code;
    }

    public ErrorCodeException(Throwable exception) {
        super(exception);
    }
    public int getCode(){
    	return this.code;
    }
}
