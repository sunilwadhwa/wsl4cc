package com.sap.ateam.wsl4cc.io;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Wsl4ccInput {
	private String type;
	private String name;
	private Map<String, Object> input;
	private Map<String, Object> tables;
	@JsonIgnore
	private UUID requestId = UUID.randomUUID();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getInput() {
		return input;
	}
	public void setInput(Map<String, Object> input) {
		this.input = input;
	}
	public Map<String, Object> getTables() {
		return tables;
	}
	public void setTables(Map<String, Object> tables) {
		this.tables = tables;
	}
	@JsonIgnore
	public UUID getRequestId() {
		return requestId;
	}
	@JsonIgnore
	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}
}
