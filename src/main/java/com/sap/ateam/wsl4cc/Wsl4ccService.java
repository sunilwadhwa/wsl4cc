package com.sap.ateam.wsl4cc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.sap.ateam.wsl4cc.io.OutputStatus;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;
import com.sap.ateam.wsl4cc.io.Wsl4ccInput;
import com.sap.ateam.wsl4cc.io.Wsl4ccOutput;
import com.sap.ateam.wsl4cc.util.ConversionUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterField;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class Wsl4ccService {

	@GET
	@Path("/{dest}/ping")
	public String pingService(@PathParam("dest") String dest) {
		String str = null;
        try {
			JCoDestination destination = JCoDestinationManager.getDestination(dest);
			str = (destination.isValid() ? "OK" : "FAILED");
		} catch (JCoException jcoe) {
		    logger.info("Exception occured while getting destination " + dest, jcoe);
			str = "UNKNOWN";
		}
        
        return str;
	}
	
	@POST
	@Path("/{dest}")
	@Consumes("application/json")
	public Wsl4ccOutput invokeService(@PathParam("dest") String dest, final Wsl4ccInput input) {
		Wsl4ccOutput output = new Wsl4ccOutput();
		JCoDestination destination = Wsl4ccDestination.getDestination(dest);
		JCoFunction func = Wsl4ccDestination.getFunction(destination, input.getMethodName());

		// Prepare input parameters
        JCoParameterList imports = func.getImportParameterList();
        Map<String,Object> map = input.getMethodParams();
        List<String> paramNames = new ArrayList<>();
        
        JCoParameterFieldIterator iterator = imports.getParameterFieldIterator();
        while (iterator.hasNextField()) {
        	JCoParameterField field = iterator.nextParameterField();
        	String name = field.getName();
        	paramNames.add(name);
        	
        	if (map != null && map.containsKey(name)) {
            	logger.debug("Found input field name {} of type {} with value {}", name, field.getTypeAsString(), map.get(name));
        		imports.setValue(name, map.get(name));
        	} else {
        		logger.debug("Setting default value for input field name {}", name);
        		imports.setValue(name, (String) null);
        	}
        }
        
        // Check validity of other parameters
        if (map != null && map.size() > 0) {
        	for (Map.Entry<String,Object> i: input.getMethodParams().entrySet()) {
        		String name = i.getKey();
        		if (!paramNames.contains(name)) {
        			return new Wsl4ccError("Unrecognized input parameter " + name);
        		}
        	}
        }
        
        // Check for input tables
        JCoParameterList tables = func.getTableParameterList();
        if (tables != null) {
        	iterator = tables.getParameterFieldIterator();
        	while (iterator.hasNextField()) {
        		JCoParameterField field = iterator.nextParameterField();
        		if (field.isImport()) {
        			logger.debug("Looking for input table {}", field.getName());
        		}
        	}
        }

        // Execute the function
        try {
        	func.execute(destination);
        	output.setStatus(OutputStatus.OK);
        } catch (JCoException jcoe) {
        	output.setStatus(OutputStatus.ERROR);
        	output.setMessage(jcoe.getMessage() == null ? "(null)" : jcoe.getMessage());
        }

        // Return output variables
        JCoParameterList exports = func.getExportParameterList();
        output.setOutput(ConversionUtil.convertParameterListToMap(exports));
        
        // Return tables
        tables = func.getTableParameterList();
        output.setTables(ConversionUtil.convertParameterListToMap(tables));
        
		return output;
	}
	
    private Logger logger = LoggerFactory.getLogger(Wsl4ccService.class);
}
