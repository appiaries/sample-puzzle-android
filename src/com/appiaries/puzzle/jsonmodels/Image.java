/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.jsonmodels;

/**
 * 
 * @author ntduc
 * 
 */
public class Image {
	/**
	 * Image ID
	 */
	private String id;

	/**
	 * MIME Type
	 */
	private String mimeType;

	/**
	 * File Name
	 */
	private String filename;

	/**
	 * Actual Image File Data
	 */
	private int data;

	/**
	 * Created Timestamp
	 */
	private Long cts;

	/**
	 * Created By
	 */
	private String cby;

	/**
	 * Updated Timestamp
	 */
	private Long uts;

	/**
	 * Updated By
	 */
	private String uby;

	public Image() {
	}

	public Image(String id, String mimeType, String filename, int data,
			Long cts, String cby, Long uts, String uby) {
		this.id = id;
		this.mimeType = mimeType;
		this.filename = filename;
		this.data = data;
		this.cts = cts;
		this.cby = cby;
		this.uts = uts;
		this.uby = uby;
	}
	
	/**
	 * 
	 * @return id String Image ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id  String Image ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return mimeType String MIME Type
	 */
	public String getMimeType() {
		return mimeType;
	}
	
	/**
	 * 
	 * @param mimeType String MIME Type
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	/**
	 * 
	 * @return filename String File Name
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * 
	 * @param filename String File Name
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * 
	 * @return data String Actual Image File Data
	 */
	public int getData() {
		return data;
	}
	
	/**
	 * 
	 * @param data String Actual Image File Data
	 */
	public void setData(int data) {
		this.data = data;
	}
	
	/**
	 * 
	 * @return cts Long Created Timestamp
	 */
	public Long getCts() {
		return cts;
	}
	
	/**
	 * 
	 * @param cts Long Created Timestamp
	 */
	public void setCts(Long cts) {
		this.cts = cts;
	}

	/**
	 * 
	 * @return cby String Created By
	 */
	public String getCby() {
		return cby;
	}

	/**
	 * 
	 * @param cby String Created By
	 */
	public void setCby(String cby) {
		this.cby = cby;
	}
	
	/**
	 * 
	 * @return uts Long Updated Timestamp
	 */
	public Long getUts() {
		return uts;
	}

	/**
	 * 
	 * @param uts Long Updated Timestamp
	 */
	public void setUts(Long uts) {
		this.uts = uts;
	}

	/**
	 * 
	 * @return uby String Updated By
	 */
	public String getUby() {
		return uby;
	}
	
	/**
	 * 
	 * @param uby String String Updated By
	 */
	public void setUby(String uby) {
		this.uby = uby;
	}
}
