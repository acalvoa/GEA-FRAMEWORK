package gea.utils.Exception;

public class ErrorDateConvertionException extends BaseException{

	private static final long serialVersionUID = 1L;
	private static final String code = "CODE DB02";

	public ErrorDateConvertionException() {
        super();
    }

    public ErrorDateConvertionException(String message) {
        super(message);
    }

    public ErrorDateConvertionException(Throwable exception) {
        super(exception);
    }

}
