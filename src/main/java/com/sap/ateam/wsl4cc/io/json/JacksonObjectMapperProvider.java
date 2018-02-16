package com.sap.ateam.wsl4cc.io.json;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultObjectMapper;

    public JacksonObjectMapperProvider() {
    	logger.debug("Registering custom JSON object mapper for rest4cc.");
        defaultObjectMapper = createDefaultMapper();
    }

    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper;
    }

    private ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        
        module.addSerializer(JCoTable.class, new JCoTableSerializer());
        module.addSerializer(JCoStructure.class, new JCoStructureSerializer());
        mapper.registerModule(module);
        
        return mapper;
    }
    
    private Logger logger = LoggerFactory.getLogger(JacksonObjectMapperProvider.class);
}
