/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.jsonmodels;

import java.io.Serializable;

/**
 * 
 * @author ntduc
 * 
 */
public class Introduction {
	/**
	 * Stage ID (Object ID)
	 */
	private String id;

	/**
	 * Stage Name
	 */
	private String content;
	
	/**
	 * order
	 */
	private String order;

	/**
	 * cby
	 */
	private String cby;

	/**
	 * uby
	 */
	private String uby;

	/**
	 * cts
	 */
	private Long cts;

	/**
	 * Created uts
	 */
	private Long uts;

	public Introduction() {
	}

	public Introduction(String id, String content, String order, String cby, String uby, Long cts, Long uts) {
		this.id = id;
		this.content = content;
		this.order = order;
		this.cby = cby;
		this.uby = uby;
		this.cts = cts;
		this.uts = uts;
	}

	/**
	 * 
	 * @return String content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 *            String content
	 */
	public void setContent(String Content) {
		this.content = Content;
	}
	
	/**
	 * 
	 * @return Id String User ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param Id
	 *            String User ID
	 */
	public void setId(String Id) {
		this.id = Id;
	}

	/**
	 * 
	 * @return order String order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 
	 * @param order
	 *            String order
	 */
	public void setOrder(String Order) {
		this.order = Order;
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
	 * @param cby
	 *            String Created By
	 */
	public void setCby(String Cby) {
		this.cby = Cby;
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
	 * @param uby
	 *            String Updated By
	 */
	public void setUby(String Uby) {
		this.uby = Uby;
	}
	
	/**
	 * 
	 * @return cts Long Created Timestamp
	 */
	public Long getCts() {
		return cts;
	}

	public void setCts(Long Cts) {
		this.cts = Cts;
	}


	/**
	 * 
	 * @return uts Long Updated uts
	 */
	public Long getUts() {
		return uts;
	}

	/**
	 * 
	 * @param uts
	 *            Long Updated Uts
	 */
	public void setUts(Long Uts) {
		this.uts = Uts;
	}

}
