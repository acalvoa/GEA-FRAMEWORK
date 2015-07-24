package gea.utils.Exception;

public class ErrorDataFormatException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorDataFormatException() {
        super();
    }

    public ErrorDataFormatException(String message) {
        super(message);
    }

    public ErrorDataFormatException(Throwable exception) {
        super(exception);
    }

}
