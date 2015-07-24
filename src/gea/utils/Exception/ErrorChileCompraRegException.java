package gea.utils.Exception;

public class ErrorChileCompraRegException extends BaseException{

	private static final long serialVersionUID = 1L;
	private static final String code = "CODE DB02";

	public ErrorChileCompraRegException() {
        super();
    }

    public ErrorChileCompraRegException(String message) {
        super(message);
    }

    public ErrorChileCompraRegException(Throwable exception) {
        super(exception);
    }

}
