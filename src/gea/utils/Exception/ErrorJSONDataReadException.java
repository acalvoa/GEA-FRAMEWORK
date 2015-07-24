package gea.utils.Exception;

public class ErrorJSONDataReadException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorJSONDataReadException() {
        super();
    }

    public ErrorJSONDataReadException(String message) {
        super(message);
    }

    public ErrorJSONDataReadException(Throwable exception) {
        super(exception);
    }

}
