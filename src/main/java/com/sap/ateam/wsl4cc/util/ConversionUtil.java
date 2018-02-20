package com.sap.ateam.wsl4cc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ateam.wsl4cc.Wsl4ccException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterField;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecord;
import com.sap.conn.jco.JCoTable;

public class ConversionUtil {

	public static Object convertJCoFieldToPrimitive(JCoField field) {
		Object ret = null;
		int type = field.getType();
		
		switch (type) {
			case JCoMetaData.TYPE_CHAR:
			case JCoMetaData.TYPE_INT1:
			case JCoMetaData.TYPE_INT2:
			case JCoMetaData.TYPE_INT8:
			case JCoMetaData.TYPE_INT:
			case JCoMetaData.TYPE_NUM:
			case JCoMetaData.TYPE_BCD:
			case JCoMetaData.TYPE_FLOAT:
			case JCoMetaData.TYPE_STRING:
			case JCoMetaData.TYPE_DECF16:
			case JCoMetaData.TYPE_DECF34:
				ret = field.getValue();
				break;
				
			case JCoMetaData.TYPE_BYTE:
			case JCoMetaData.TYPE_XSTRING:
			case JCoMetaData.TYPE_DATE:
			case JCoMetaData.TYPE_TIME:
				ret = field.getString();
				break;

			case JCoMetaData.TYPE_STRUCTURE:
				ret = field.getStructure();
				break;
				
			case JCoMetaData.TYPE_TABLE:
				ret = field.getTable();
				break;
				
			default:
				ret = null;
				logger.error("Unrecognized type " + field.getClassNameOfValue() + " for field " + field.getName());
				break;
		}
		
		return ret;
	}
	
	/**
	 * Convert a user provided List to a JCoTable input table.
	 * 
	 * @param tableName Name of input table parameter
	 * @param table JCoTable 
	 * @param list User provided list
	 * @return Resulting table
	 * @throws Wsl4ccException
	 */
	public static JCoTable convertToJCoTable(String tableName, JCoTable table, List<?> list) throws Wsl4ccException {
		if (list == null || table == null)
			return table;
		
		JCoMetaData meta = table.getMetaData();
		for (Object o : list) {
			if (!(o instanceof Map<?,?>))
				throw new Wsl4ccException("User input for table " + tableName + " is not a valid map.");
			
			Map<?,?> map = (Map<?,?>) o;
			table.appendRow();
			for (Map.Entry<?,?> e : map.entrySet()) {
				String fieldName = (String) e.getKey();
				Object fieldValue = (Object) e.getValue();
				
				if (meta.hasField(fieldName))
					convertPrimitiveToJCoField(table, fieldName, fieldValue);
				else
					throw new Wsl4ccException("Unrecognized or invalid field " + fieldName + " in table " + tableName);
			}
		}
		
		return table;
	}
	
	public static void convertPrimitiveToJCoField(JCoRecord record, String name, Object value) {
		JCoField field = record.getField(name);
		
		switch (field.getType()) {
			case JCoMetaData.TYPE_DATE:
				Date d = DateUtils.parseDate((String) value, new String[] {"YYYY-MM-DD"}); // TODO: Make dateFormat a query parameter.
				record.setValue(name, d);
				break;

			default:
				record.setValue(name, value);
				break;
		}
	}

	public static Map<String,Object> convertParameterListToMap(JCoParameterList pList) {
		if (pList == null) return null;
		
        Map <String,Object> pMap = new HashMap<>();
        JCoParameterFieldIterator iterator = pList.getParameterFieldIterator();
        
        while (iterator.hasNextField()) {
        	JCoParameterField field = iterator.nextParameterField();
			Object value = convertJCoFieldToPrimitive(field);
        	pMap.put(field.getName(), value);
        }
        
        return pMap;
	}
	
    private static Logger logger = LoggerFactory.getLogger(ConversionUtil.class);

}
