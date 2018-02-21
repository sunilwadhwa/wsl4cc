package com.sap.ateam.wsl4cc.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ateam.wsl4cc.io.Wsl4ccError;

@Provider
public class UncaughtExceptionHandler implements ExceptionMapper<Throwable> {

	/**
	 * This method will be called whenever an uncaught runtime exception is thrown. It gracefully returns HTTP code
	 * 500 and a message explaining the internal server error.
	 */
	@Override
	public Response toResponse(Throwable t) {
		String message = (t.getMessage() != null) ? t.getMessage() : t.getClass().getName();
		Wsl4ccError error = new Wsl4ccError("An Internal Server Error has occurred: " + message);
		
		logger.error("Uncaught exception error encountered.", t);
		
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(error)
				.type("application/json")
				.build();
	}
	
    private Logger logger = LoggerFactory.getLogger(UncaughtExceptionHandler.class);
}
