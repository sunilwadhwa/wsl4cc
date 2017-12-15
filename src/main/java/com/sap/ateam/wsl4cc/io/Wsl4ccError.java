package com.sap.ateam.wsl4cc.io;

public class Wsl4ccError extends Wsl4ccOutput {
	public Wsl4ccError (String message) {
		super();
		setStatus(OutputStatus.ERROR);
		setMessage(message);
	}
}
