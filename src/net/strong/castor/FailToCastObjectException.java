package net.strong.castor;

@SuppressWarnings("serial")
public class FailToCastObjectException extends RuntimeException {

	public FailToCastObjectException(String message) {
		super(message);
	}

	public FailToCastObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
