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
import com.sap.conn.jco.JCoTable;

public class JCoTableSerializer extends StdSerializer<JCoTable> {

	private static final long serialVersionUID = -6739422571165264686L;
	
	public JCoTableSerializer() {
		this(null);
	}
	
	protected JCoTableSerializer(Class<JCoTable> c) {
		super(c);
	}

	@Override
	public void serialize(JCoTable table, JsonGenerator jgen, SerializerProvider sprovider) throws IOException {
		int i = 0;
		
		logger.debug("Writing table {} with {} row(s)", table.getRecordMetaData().getName(), table.getNumRows());
		
		jgen.writeStartArray();

		for (i = 0, table.setRow(i); i < table.getNumRows(); i++, table.setRow(i)) {
			JCoRecordFieldIterator iterator = table.getRecordFieldIterator();
			jgen.writeStartObject();
			while (iterator.hasNextField()) {
				JCoRecordField field = iterator.nextRecordField();
				Object value = ConversionUtil.convertJCoFieldToPrimitive(field);
				jgen.writeObjectField(field.getName(), value);
			}
			jgen.writeEndObject();
		}
		
		jgen.writeEndArray();
	}

    private Logger logger = LoggerFactory.getLogger(JCoTableSerializer.class);
}
