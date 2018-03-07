package com.sap.ateam.wsl4cc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.sap.ateam.wsl4cc.handler.ServiceHandler;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;
import com.sap.ateam.wsl4cc.io.Wsl4ccInput;
import com.sap.ateam.wsl4cc.io.Wsl4ccOutput;
import com.sap.ateam.wsl4cc.ping.PingServiceHandler;
import com.sap.ateam.wsl4cc.rfc.RfcServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class Wsl4ccService {

	@GET
	@Path("/destinations/{dest}/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Wsl4ccOutput pingService(@PathParam("dest") String dest) {
		Wsl4ccOutput output = null;
		ServiceHandler pingServiceHandler = new PingServiceHandler();
		pingServiceHandler.initialize(dest);

		try {
			output = pingServiceHandler.execute(null);
		} catch (Wsl4ccException e) {
		    logger.error("Exception occured while getting destination " + dest, e);
			output = new Wsl4ccError("Exception occured while getting destination " + dest + ": " + e.getMessage());
		}

		return output;
	}

	@POST
	@Path("/destinations/{dest}/rfc")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Wsl4ccOutput invokeRfcService(@PathParam("dest") String dest, final Wsl4ccInput input) {
		Wsl4ccOutput output = null;
		ServiceHandler rfcServiceHandler = new RfcServiceHandler();
		rfcServiceHandler.initialize(dest);

		try {
			output = rfcServiceHandler.execute(input);
		} catch (Wsl4ccException e) {
			logger.error("Exception occured while executing RFC service on destination " + dest, e);
			output = new Wsl4ccError("Exception occured while executing RFC service on destination " + dest + ": " + e.getMessage());
		}

		return output;
	}

    private Logger logger = LoggerFactory.getLogger(Wsl4ccService.class);
}
