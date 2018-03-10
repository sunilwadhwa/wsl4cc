package com.sap.ateam.wsl4cc.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;

@Provider
public class UnrecognizedPropertyExceptionHandler implements ExceptionMapper<UnrecognizedPropertyException> {

	/**
	 * This method will be called whenever an input property is unrecognized. It gracefully returns HTTP code
	 * 400 and a message identifying the unrecognized property name.
	 */
	@Override
	public Response toResponse(UnrecognizedPropertyException upe) {
		Wsl4ccError error = new Wsl4ccError("Unrecognized input property '" + upe.getPropertyName() + "'.");
		
		logger.warn("Unrecognized input property '{}'.", upe.getPropertyName(), upe);
		
		return Response
				.status(Status.BAD_REQUEST)
				.entity(error)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
    private Logger logger = LoggerFactory.getLogger(UnrecognizedPropertyExceptionHandler.class);
}
