package gea.utils.Exception;

public class ErrorDBDataNotExistsException extends BaseException{

	private static final long serialVersionUID = 1L;
	private static final String code = "CODE DB01";

	public ErrorDBDataNotExistsException() {
        super();
    }

    public ErrorDBDataNotExistsException(String message) {
        super(message);
    }

    public ErrorDBDataNotExistsException(Throwable exception) {
        super(exception);
    }

}
