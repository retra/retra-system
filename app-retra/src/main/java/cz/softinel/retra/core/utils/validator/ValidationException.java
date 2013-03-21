package cz.softinel.retra.core.utils.validator;

//TODO: make as runtime exception
@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {

	public ValidationException() {
		super();
	}
	
    public ValidationException(String message) {
    	super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }


    public ValidationException(Throwable cause) {
        super(cause);
    }
}
