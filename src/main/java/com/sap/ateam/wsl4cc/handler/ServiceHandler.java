package com.sap.ateam.wsl4cc.handler;

import com.sap.ateam.wsl4cc.Wsl4ccException;
import com.sap.ateam.wsl4cc.io.Wsl4ccInput;
import com.sap.ateam.wsl4cc.io.Wsl4ccOutput;

public interface ServiceHandler {
	void initialize(String dest);
	Wsl4ccOutput execute(Wsl4ccInput input) throws Wsl4ccException;
}
