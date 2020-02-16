package com.tavant;
	
	import java.io.IOException;
	import java.text.DateFormat;
	import java.util.List;
	import java.util.Map;
	import java.util.Set;

	 

	import com.fasterxml.jackson.annotation.JsonAutoDetect;
	import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
	import com.fasterxml.jackson.annotation.PropertyAccessor;
	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.core.type.TypeReference;
	import com.fasterxml.jackson.databind.DeserializationFeature;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
	import java.lang.Exception;

	 


	public class JSONUtils {
	    
	    private JSONUtils() {
	        
	    }
	    
	    public static String mapToJson(Map<String, Object> map) {
	        return convertToJson(map);
	    }
	    
	    public static Map<String, Object> JsonToMap(String json) {
	        Map<String, Object> map = null;
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
	        try {
	            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
	            });
	        } catch (IOException e) {
	             System.out.println(e);
	        }
	        return map;
	    }
	    
	    public static String listToJson(List<Map<String, Object>> map) {
	        return convertToJson(map);
	    }
	    
	    public static List<Map<String, Object>> jsonToList(String json) {
	        return jsonToList(json, new TypeReference<List<Map<String, Object>>>() {
	        });
	    }
	    
	    public static <T> List<T> jsonToList(String json, TypeReference<List<T>> type) {
	        List<T> list = null;
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
	        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        try {
	            list = mapper.readValue(json, type);
	        } catch (IOException e) {
	             System.out.println(e);
	        }
	        return list;
	    }
	    
	    public static List<Map<String, Object>> jsonToList(String json, Class<?> listClass, Class<?> genericClass) {
	        List<Map<String, Object>> list = null;
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
	        try {
	            list = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
	            });
	        } catch (IOException e) {
	             System.out.println(e);
	        }
	        return list;
	    }
	    
	    /**
	     * @param map
	     * @return
	     */
	    private static String convertToJson(Object map) {
	        String json = null;
	        try {
	            ObjectMapper myObjectMapper = new ObjectMapper();
	           // myObjectMapper.setDateFormat(ParseDate.getDateFormat());
	            json = null != map ? myObjectMapper.writeValueAsString(map) : null;
	        } catch (JsonProcessingException e) {
	             System.out.println(e);
	        }
	        return json;
	    }

	 

	    /**
	     * @param map
	     * @return
	     */
	    private static String convertToJson(Object map, DateFormat dateFormat) {
	        String json = null;
	        try {
	            ObjectMapper myObjectMapper = new ObjectMapper();
	            myObjectMapper.setDateFormat(dateFormat);
	            json = null != map ? myObjectMapper.writeValueAsString(map) : null;
	        } catch (JsonProcessingException e) {
	             System.out.println(e);
	        }
	        return json;
	    }

	 

	    public static <T> T toObject(String json, Class<T> classType) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	          //  mapper.setDateFormat(ParseDate.getDateFormat());
	           // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	            return mapper.readValue(json, classType);
	        } catch (Exception e) {
	             System.out.println(e);
	        }
	        return null;
	    }
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    public static Object toObjectWithVisibility(String json, Class classType) {
	        Object obj = null;
	        try {
	            ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	            obj = mapper.readValue(json, classType);
	        } catch (Exception e) {
	             System.out.println(e);
	        }
	        return obj;
	    }
	    
	    @SuppressWarnings("unchecked")
	    public static Object toObjectIgnoreUnknownFields(String json, Class classType) {
	        Object obj = null;
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	            mapper.setVisibility(
	                    VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
	            obj = mapper.readValue(json, classType);
	            
	        } catch (Exception e) {
	             System.out.println(e);
	        }
	        return obj;
	    }
	    
	    public static String toJSON(Object obj) {
	        return convertToJson(obj);
	    }

	 

	    public static String toJSON(Object obj, DateFormat dateFormat) {
	        return convertToJson(obj, dateFormat);
	    }

	 

	    public static Object toObjectList(String json, TypeReference<?> type) {
	        Object obj = null;
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	            mapper.setVisibility(
	                    VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
	            obj = mapper.readValue(json, type);
	            
	        } catch (Exception e) {
	           System.out.println(e);
	        }
	        return obj;
	    }
	    
	    public static List<Object> jsonToListObject(String json)  {
	        List<Object> list = null;
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            list = mapper.readValue(json, new TypeReference<List<Object>>() {
	            });
	        } catch (IOException e) {
	             System.out.println(e);
	        }
	        return list;
	    }
	    
	    public static <T> Set<T> jsonToSetList(String json){
	        Set<T> list = null;
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            list = mapper.readValue(json, new TypeReference<Set<T>>() {
	            });
	        } catch (IOException e) {
	             System.out.println(e);
	        }
	        return list;
	    }
	}
