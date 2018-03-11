package com.sap.ateam.wsl4cc.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;

@Provider
public class JsonMappingExceptionHandler implements ExceptionMapper<JsonMappingException> {

	/**
	 * This method will be called when an input has a mismatched datatype. It gracefully returns HTTP code
	 * 400 and a message identifying the issue.
	 */
	@Override
	public Response toResponse(JsonMappingException jme) {
		Wsl4ccError error = new Wsl4ccError("Error processing the input JSON. " + jme.getMessage());
		
		logger.warn("Error processing the input JSON. " + jme.getMessage());
		
		return Response
				.status(Status.BAD_REQUEST)
				.entity(error)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
    private Logger logger = LoggerFactory.getLogger(JsonMappingExceptionHandler.class);
}
