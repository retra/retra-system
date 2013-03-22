package cz.softinel.retra.core.utils.convertor;

//TODO: make as runtime exception
@SuppressWarnings("serial")
public class ConvertException extends Exception {

	public ConvertException() {
		super();
	}
	
    public ConvertException(String message) {
    	super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }


    public ConvertException(Throwable cause) {
        super(cause);
    }
}
