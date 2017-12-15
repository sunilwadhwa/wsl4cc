package com.sap.ateam.wsl4cc;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.sap.ateam.wsl4cc.io.OutputStatus;
import com.sap.ateam.wsl4cc.io.Rest4ccInput;
import com.sap.ateam.wsl4cc.io.Rest4ccOutput;
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
public class Rest4ccService {

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
	public Rest4ccOutput invokeService(@PathParam("dest") String dest, final Rest4ccInput input) {
		Rest4ccOutput output = new Rest4ccOutput();
		JCoDestination destination = Rest4ccDestination.getDestination(dest);
		JCoFunction func = Rest4ccDestination.getFunction(destination, input.getMethodName());

		// Prepare input parameters
        JCoParameterList imports = func.getImportParameterList();
        
        JCoParameterFieldIterator iterator = imports.getParameterFieldIterator();
        while (iterator.hasNextField()) {
        	JCoParameterField field = iterator.nextParameterField();
        	logger.debug("Looking for field name {} of type {}", field.getName(), field.getTypeAsString());
        }
        
        Map<String,Object> map = input.getMethodParams();
        if (map != null && map.size() > 0) {
        	for (Map.Entry<String,Object> i: input.getMethodParams().entrySet()) {
        		String name = i.getKey();
        		Object value = i.getValue();
                imports.setValue(name, value);
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
        JCoParameterList tables = func.getTableParameterList();
        output.setTables(ConversionUtil.convertParameterListToMap(tables));
        
		return output;
	}
	
    private Logger logger = LoggerFactory.getLogger(Rest4ccService.class);
}
