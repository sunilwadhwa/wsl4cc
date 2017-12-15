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

        try {
        	// mapper.addMixIn(JCoField.class, AbstractFieldMixIn.class);
        	// mapper.getSerializationConfig().addMixInAnnotations(Class.forName("com.sap.conn.jco.rt.AbstractField"), AbstractFieldMixIn.class);
        	// mapper.getDeserializationConfig().addMixInAnnotations(Class.forName("com.sap.conn.jco.rt.AbstractField"), AbstractFieldMixIn.class);
        } catch (Exception e) {
        	logger.warn("MixIn registration for AbstractField failed.", e);
        }

        try {
        	// mapper.addMixIn(Class.forName("com.sap.conn.jco.rt.DefaultStructure"), DefaultStructureMixIn.class);
        	// mapper.getSerializationConfig().addMixInAnnotations(Class.forName("com.sap.conn.jco.rt.DefaultStructure"), DefaultStructureMixIn.class);
        	// mapper.getDeserializationConfig().addMixInAnnotations(Class.forName("com.sap.conn.jco.rt.DefaultStructure"), DefaultStructureMixIn.class);
        } catch (Exception e) {
        	logger.warn("MixIn registration for DefaultStructure failed.", e);
        }

        SimpleModule module = new SimpleModule();
        module.addSerializer(JCoTable.class, new JCoTableSerializer());
        module.addSerializer(JCoStructure.class, new JCoStructureSerializer());
        mapper.registerModule(module);
        
        return mapper;
    }
    
    private Logger logger = LoggerFactory.getLogger(JacksonObjectMapperProvider.class);
}
