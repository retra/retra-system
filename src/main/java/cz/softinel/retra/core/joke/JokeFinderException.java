package cz.softinel.retra.core.joke;

@SuppressWarnings("serial")
public class JokeFinderException extends Exception {

	public JokeFinderException() {
		super();
	}

	public JokeFinderException(String message) {
		super(message);
	}

	public JokeFinderException(String message, Throwable cause) {
		super(message, cause);
	}

	public JokeFinderException(Throwable cause) {
		super(cause);
	}
}
