package com.sap.ateam.wsl4cc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ateam.wsl4cc.Wsl4ccException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterField;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

public class ConversionUtil {

	public static Object convertJCoFieldToPrimitive(JCoField field, int type) {
		Object ret = null;
		
		switch (type) {
			case JCoMetaData.TYPE_CHAR:
			case JCoMetaData.TYPE_INT1:
			case JCoMetaData.TYPE_INT2:
			case JCoMetaData.TYPE_INT8:
			case JCoMetaData.TYPE_INT:
			case JCoMetaData.TYPE_NUM:
			case JCoMetaData.TYPE_BCD:
			case JCoMetaData.TYPE_FLOAT:
			case JCoMetaData.TYPE_DATE:
			case JCoMetaData.TYPE_TIME:
			case JCoMetaData.TYPE_STRING:
			case JCoMetaData.TYPE_DECF16:
			case JCoMetaData.TYPE_DECF34:
				ret = field.getValue();
				break;
				
			case JCoMetaData.TYPE_BYTE:
			case JCoMetaData.TYPE_XSTRING:
				ret = field.getString();
				break;

			case JCoMetaData.TYPE_STRUCTURE:
				ret = field.getStructure();
				break;
				
			case JCoMetaData.TYPE_TABLE:
				ret = field.getTable();
				break;
				
			default:
				ret = "UNRECOGNIZED TYPE:" + field.getClassNameOfValue();
				break;
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static JCoTable convertToJCoTable(JCoTable table, Object object) throws Wsl4ccException {
		if (!(object instanceof List<?>))
			throw new Wsl4ccException("User input was not a list.");
		
		List<Object> list = (List<Object>) object;
		for (Object o : list) {
			if (o instanceof Map<?,?>) {
				Map<String, Object> map = (Map<String,Object>) o;
				table.appendRow();
				for (Map.Entry<String, Object> e : map.entrySet()) {
					table.setValue(e.getKey(), e.getValue());
				}
			} else {
				throw new Wsl4ccException("User input was not a map.");
			}
		}
		return table;
	}
	
	public static Map<String,Object> convertParameterListToMap(JCoParameterList pList) {
		if (pList == null) return null;
		
        Map <String,Object> pMap = new HashMap<>();
        JCoParameterFieldIterator iterator = pList.getParameterFieldIterator();
		JCoListMetaData meta = pList.getListMetaData();
        
        while (iterator.hasNextField()) {
        	JCoParameterField field = iterator.nextParameterField();
			Object value = convertJCoFieldToPrimitive(field, meta.getType(field.getName()));
			logger.debug("Adding field " + field.getName() + ", class = " + field.getClassNameOfValue() + ", size = " + (field.isTable() ? ((JCoTable) value).getNumRows() : 0));
        	pMap.put(field.getName(), value);
        }
        
        return pMap;
	}
	
    private static Logger logger = LoggerFactory.getLogger(ConversionUtil.class);

}
