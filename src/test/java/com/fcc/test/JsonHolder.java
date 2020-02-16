package com.fcc.test;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonHolder {
	private static final Logger logger = Logger.getLogger(JsonHolder.class) ;
	private static ThreadLocal<JsonHolder>  jsonHolder=new ThreadLocal();
	
	public String jsonString="";
	private JSONParser jsonParser=null;
	private JSONObject jsonObject=null;
	
	public String getJsonString() {
		return getInstance().jsonString;
	}

	public void setJsonString(String _jsonString) throws Exception {
		getInstance().jsonString = _jsonString;
		try {
			getInstance().jsonParser = new JSONParser();
			getInstance().jsonObject = (JSONObject) getInstance().jsonParser.parse(getInstance().jsonString);
		} catch (ParseException e) {
			logger.error(e);
			logger.error("Unable to Parse Json String");
			throw new Exception(e);
		}
	}

	public JSONParser getJsonParser() {
		return getInstance().jsonParser;
	}

	public void setJsonParser(JSONParser _jsonParser) {
		getInstance().jsonParser = _jsonParser;
	}

	public JSONObject getJsonObject() {
		return getInstance().jsonObject;
	}

	public void setJsonObject(JSONObject _jsonObject) {
		getInstance().jsonObject = _jsonObject;
	}

	
	
	public JsonHolder() {
		
	}

	public static JsonHolder getInstance() {
		if (jsonHolder.get()==null)
			jsonHolder.set(new JsonHolder()); 
		return (JsonHolder) jsonHolder.get();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
