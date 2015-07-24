package gea.utils.Exception;

public class ErrorDataException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorDataException() {
        super();
    }

    public ErrorDataException(String message) {
        super(message);
    }

    public ErrorDataException(Throwable exception) {
        super(exception);
    }

}
