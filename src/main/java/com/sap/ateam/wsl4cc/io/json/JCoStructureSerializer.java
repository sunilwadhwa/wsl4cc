package com.sap.ateam.wsl4cc.io.json;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sap.ateam.wsl4cc.util.ConversionUtil;
import com.sap.conn.jco.JCoRecordField;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoStructure;

public class JCoStructureSerializer extends StdSerializer<JCoStructure> {

	private static final long serialVersionUID = -3568273669673114660L;

	public JCoStructureSerializer() {
		this(null);
	}
	
	protected JCoStructureSerializer(Class<JCoStructure> c) {
		super(c);
	}

	@Override
	public void serialize(JCoStructure struct, JsonGenerator jgen, SerializerProvider sprovider) throws IOException {
		
		logger.debug("Writing structure {} with {} field(s)", struct.getRecordMetaData().getName(), struct.getFieldCount());
		
		JCoRecordFieldIterator iterator = struct.getRecordFieldIterator();
		jgen.writeStartObject();
		
		while (iterator.hasNextField()) {
			JCoRecordField field = iterator.nextRecordField();
			Object value = ConversionUtil.convertJCoFieldToPrimitive(field);
			jgen.writeObjectField(field.getName(), value);
		}
		
		jgen.writeEndObject();
	}

    private Logger logger = LoggerFactory.getLogger(JCoStructureSerializer.class);
}
