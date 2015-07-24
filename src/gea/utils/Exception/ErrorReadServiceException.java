package gea.utils.Exception;

public class ErrorReadServiceException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorReadServiceException() {
        super();
    }

    public ErrorReadServiceException(String message) {
        super(message);
    }

    public ErrorReadServiceException(Throwable exception) {
        super(exception);
    }

}
