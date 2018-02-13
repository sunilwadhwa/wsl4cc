package com.sap.ateam.wsl4cc.rfc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ateam.wsl4cc.Wsl4ccDestination;
import com.sap.ateam.wsl4cc.Wsl4ccException;
import com.sap.ateam.wsl4cc.handler.ServiceHandler;
import com.sap.ateam.wsl4cc.io.OutputStatus;
import com.sap.ateam.wsl4cc.io.Wsl4ccError;
import com.sap.ateam.wsl4cc.io.Wsl4ccInput;
import com.sap.ateam.wsl4cc.io.Wsl4ccOutput;
import com.sap.ateam.wsl4cc.util.ConversionUtil;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterField;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

public class RfcServiceHandler implements ServiceHandler {

	@Override
	public void initialize(String dest) {
		this.dest = dest;
	}

	@Override
	public Wsl4ccOutput execute(Wsl4ccInput input) throws Wsl4ccException {
		JCoDestination destination = Wsl4ccDestination.getDestination(dest);
		JCoFunction func = Wsl4ccDestination.getFunction(destination, input.getMethodName());
		
		if (func == null)
			return new Wsl4ccError("Unrecognized RFC function " + input.getMethodName());

		// Prepare input parameters
		Wsl4ccOutput output = new Wsl4ccOutput();
        prepareImportsFromUserInput (func, input);
        
        // Prepare input tables, if any
        prepareTablesFromUserInput (func, input);
        

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

	private void prepareImportsFromUserInput(JCoFunction func, Wsl4ccInput input) throws Wsl4ccException {
        JCoParameterList imports = func.getImportParameterList();
        Map<String,Object> userInputMap = input.getMethodParams();
        List<String> paramNames = new ArrayList<>();
        
        JCoParameterFieldIterator iterator = imports.getParameterFieldIterator();
        while (iterator.hasNextField()) {
        	JCoParameterField field = iterator.nextParameterField();
        	String name = field.getName();
        	paramNames.add(name);
        	
        	if (userInputMap != null && userInputMap.containsKey(name)) {
            	logger.debug("Found input field name {} of type {} with value {}", name, field.getTypeAsString(), userInputMap.get(name));
        		imports.setValue(name, userInputMap.get(name));
        	} else {
        		logger.debug("Setting default value for input field name {}", name);
        		imports.setValue(name, (String) null);
        	}
        }
        
        // Check validity of other parameters
        if (userInputMap != null && userInputMap.size() > 0) {
        	for (Map.Entry<String,Object> i: input.getMethodParams().entrySet()) {
        		String name = i.getKey();
        		if (!paramNames.contains(name)) {
        			throw new Wsl4ccException ("Unrecognized input parameter " + name);
        		}
        	}
        }
	}

	private void prepareTablesFromUserInput(JCoFunction func, Wsl4ccInput input) throws Wsl4ccException {
        JCoParameterList tables = func.getTableParameterList();
        Map<String,Object> userTableMap = input.getMethodTables();
        List<String> tableNames = new ArrayList<>();
        
        JCoParameterFieldIterator iterator = tables.getParameterFieldIterator();
        while (iterator.hasNextField()) {
        	JCoParameterField field = iterator.nextParameterField();
        	String name = field.getName();
        	tableNames.add(name);
        	
        	if (userTableMap != null && userTableMap.containsKey(name)) {
            	logger.debug("Found input table name {} of type {} with value {}", name, field.getTypeAsString(), userTableMap.get(name));
            	JCoTable table = tables.getTable(name);
            	ConversionUtil.convertToJCoTable(table, userTableMap.get(name));
        		tables.setValue(name, table);
        	} else {
        		logger.debug("Setting default value for input table name {}", name);
        		// tables.setValue(name, (JCoTable) null);
        	}
        }
        
        // Check validity of other parameters
        if (userTableMap != null && userTableMap.size() > 0) {
        	for (Map.Entry<String,Object> i: input.getMethodTables().entrySet()) {
        		String name = i.getKey();
        		if (!tableNames.contains(name)) {
        			throw new Wsl4ccException ("Unrecognized table parameter " + name);
        		}
        	}
        }

	}

	private String dest;
    private Logger logger = LoggerFactory.getLogger(RfcServiceHandler.class);
}
