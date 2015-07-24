package gea.utils.Exception;

public class Error403Exception extends BaseException{

	private static final long serialVersionUID = 1L;

	public Error403Exception() {
		super();
		this.printStackTrace();
    }

    public Error403Exception(String message) {
        super(message);
    }

    public Error403Exception(Throwable exception) {
    	super(exception);
    	
        
    }

}
