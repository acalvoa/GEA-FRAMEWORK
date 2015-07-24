package gea.utils.Exception;


public class GeneralException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeneralException() {
        super();
      }

      public GeneralException(String message) {
        super(message);
      }

      public GeneralException(Throwable exception) {
        super(exception);
    }
	

}