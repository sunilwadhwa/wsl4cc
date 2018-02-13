package com.sap.ateam.wsl4cc;

public class Wsl4ccException extends Exception {

	private static final long serialVersionUID = -3821213736848652249L;

	public Wsl4ccException() {
	}

	public Wsl4ccException(String message) {
		super(message);
	}

	public Wsl4ccException(Throwable cause) {
		super(cause);
	}

	public Wsl4ccException(String message, Throwable cause) {
		super(message, cause);
	}

	public Wsl4ccException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
