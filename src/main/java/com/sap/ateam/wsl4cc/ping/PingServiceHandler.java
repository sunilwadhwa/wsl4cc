package com.sap.ateam.wsl4cc.ping;

import com.sap.ateam.wsl4cc.Wsl4ccException;
import com.sap.ateam.wsl4cc.handler.ServiceHandler;
import com.sap.ateam.wsl4cc.io.OutputStatus;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;
import com.sap.ateam.wsl4cc.io.Wsl4ccInput;
import com.sap.ateam.wsl4cc.io.Wsl4ccOutput;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

public class PingServiceHandler implements ServiceHandler {

	@Override
	public void initialize(String dest) {
		this.dest = dest;
	}

	/* TODO: (med) Distinguish between HTTP destinations and RFC destinations. */
	@Override
	public Wsl4ccOutput execute(Wsl4ccInput input) throws Wsl4ccException {
		Wsl4ccOutput output = new Wsl4ccOutput();
		JCoDestination destination;
		
		try {
			destination = JCoDestinationManager.getDestination(dest);
			if (destination != null && destination.isValid())
				output.setStatus(OutputStatus.OK);
		} catch (JCoException e) {
			output = new Wsl4ccError("Invalid destination " + dest + ": " + e.getMessage());
		}
		
		return output;
	}

	private String dest;
}
