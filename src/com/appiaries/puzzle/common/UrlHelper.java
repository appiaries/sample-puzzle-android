/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class UrlHelper {
	private String query = "";
	
	public UrlHelper(){}
	
	public UrlHelper(String name, String value) {
		encode(name, value);
	}

	public void addQuery(String name, String value) {
		query += "&";
		encode(name, value);
	}

	private void encode(String name, String value) {
		try {
			query += URLEncoder.encode(name, "UTF-8");
			query += "=";
			query += URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("Broken VM does not support UTF-8");
		}
	}

	public String getQuery() {
		return query;
	}

	public String toString() {
		return getQuery();
	}
	
	/**
	 * Parse URL ref string to HashMap object
	 * @param refString
	 * @return HashMap object
	 */
	public static HashMap<String, String> getRefValues(String refString)
	{
		String[] refs = refString.split("&");
		
		HashMap<String, String> dic = new HashMap<String, String>();	
		
		for (String ref : refs)
		{
			String[] tempArray = ref.split("=");
			
			dic.put(tempArray[0], tempArray[1]);
		}	
						
		return dic;
	}
	
	public static JSONObject getJsonObjectFromMap(HashMap<String,String> params) throws JSONException {

	    Iterator<?> iter = params.entrySet().iterator();

	    //Stores JSON
	    JSONObject holder = new JSONObject();	 

	    //While there is another entry
	    while (iter.hasNext()) 
	    {
	        //gets an entry in the params
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)iter.next();

	        //creates a key for Map
	        String key = (String)pairs.getKey();

	        //Create a new map
	        Map<?, ?> m = (Map<?, ?>)pairs.getValue();   

	        //object for storing Json
	        JSONObject data = new JSONObject();

	        //gets the value
	        Iterator<?> iter2 = m.entrySet().iterator();
	        while (iter2.hasNext()) 
	        {
	            @SuppressWarnings("rawtypes")
				Map.Entry pairs2 = (Map.Entry)iter2.next();
	            data.put((String)pairs2.getKey(), (String)pairs2.getValue());
	        }
	
	        holder.put(key, data);
	    }
	    return holder;
	}
}
