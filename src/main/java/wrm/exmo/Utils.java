package wrm.exmo;



/**
 * bean-centric utility methods
 * @author pemucha
 *
 */
public class Utils {

	
//
//	/**
//	 * returns a formatted JSON-String that represents the given bean
//	 */
//	public static String stringifyBean(Object bean){
//		
//		try {
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			writeJSONData(os, bean);
//			return os.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
//	
//	
//	
//	/**
//	 * deserializes JSON Data 
//	 */
//	public static <T> T loadJSONData(InputStream jsonString, Class<T> type) throws Exception {
//		ObjectMapper jsonMapper =  new ObjectMapper();
//		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
//		jsonMapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
//		jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//		jsonMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
//		
//		
//		T data = jsonMapper.readValue(jsonString, type);
//
//		return data;
//	}
//	
//
//	/**
//	 * serializes JSON Data
//	 */
//	public static void writeJSONData(OutputStream output, Object value) throws Exception {
//		ObjectMapper jsonMapper =  new ObjectMapper();
//		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
//		jsonMapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
//		jsonMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
//		jsonMapper.configure(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY, true);
//		jsonMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
//		jsonMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
//		
//	
//		
//		jsonMapper.writerWithDefaultPrettyPrinter().writeValue(output, value);
//
//	}



}
