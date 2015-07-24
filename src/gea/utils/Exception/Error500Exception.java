package gea.utils.Exception;

public class Error500Exception extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public Error500Exception() {
        super();
    }

    public Error500Exception(String message) {
        super(message);
    }

    public Error500Exception(Throwable exception) {
        super(exception);
    }
}
