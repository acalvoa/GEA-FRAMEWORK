package gea.utils.Exception;

public class Error404Exception extends BaseException{

	private static final long serialVersionUID = 1L;

	public Error404Exception() {
        super();
    }

    public Error404Exception(String message) {
        super(message);
    }

    public Error404Exception(Throwable exception) {
        super(exception);
    }

}
