package com.sap.ateam.wsl4cc.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;

@Provider
public class JsonParseExceptionHandler implements ExceptionMapper<JsonParseException> {

	/**
	 * This method will be called when an input is invalid/malformed JSON. It gracefully returns HTTP code
	 * 400 and a message identifying the syntax issue.
	 */
	@Override
	public Response toResponse(JsonParseException jpe) {
		Wsl4ccError error = new Wsl4ccError("Input is not well-formed JSON. " + jpe.getMessage());
		
		logger.warn("Input is not well-formed JSON. " + jpe.getMessage());
		
		return Response
				.status(Status.BAD_REQUEST)
				.entity(error)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
    private Logger logger = LoggerFactory.getLogger(JsonParseExceptionHandler.class);
}
