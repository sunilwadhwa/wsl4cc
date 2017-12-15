package com.sap.ateam.wsl4cc.io;

import java.util.Map;

public class Rest4ccOutput {
	private OutputStatus status;
	private String message;
	private Map<String,Object> output;
	private Map<String,Object> tables;
	
	public OutputStatus getStatus() {
		return status;
	}
	public void setStatus(OutputStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getOutput() {
		return output;
	}
	public void setOutput(Map<String, Object> output) {
		this.output = output;
	}
	public Map<String, Object> getTables() {
		return tables;
	}
	public void setTables(Map<String, Object> tables) {
		this.tables = tables;
	}
}
