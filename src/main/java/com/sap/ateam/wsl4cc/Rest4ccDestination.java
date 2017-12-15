package com.sap.ateam.wsl4cc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;

public class Rest4ccDestination {

	public static JCoDestination getDestination(String dest) {
        try {
			JCoDestination destination = JCoDestinationManager.getDestination(dest);
			return destination;
		} catch (JCoException jcoe) {
		    logger.info("Exception occured while getting destination " + dest, jcoe);
		}
		return null;
	}
	
	public static JCoFunction getFunction (JCoDestination destination, String functionName) {
        JCoRepository repo = null;
        JCoFunction func = null;
        
        try {
        	if (destination != null)
        		repo = destination.getRepository();
        	
        	if (repo != null)
        		func = repo.getFunction(functionName);
		} catch (JCoException jcoe) {
			logger.error("Exception occured while getting function " + functionName, jcoe);
		}
        
        return func;
	}

    private static Logger logger = LoggerFactory.getLogger(Rest4ccDestination.class);
}
