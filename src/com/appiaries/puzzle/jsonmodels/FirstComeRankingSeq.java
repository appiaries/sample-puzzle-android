/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.jsonmodels;

/**
 * 
 * @author ntduc
 * 
 */
public class FirstComeRankingSeq {

	/**
	 * First-Come Ranking Sequence ID (Object ID)
	 */
	private String id;

	/**
	 * value
	 */
	private Long value;

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

	/**
	 * init
	 */
	public FirstComeRankingSeq() {
	}

	/**
	 * 
	 * @param id
	 * @param value
	 * @param cts
	 * @param cby
	 * @param uts
	 * @param uby
	 */
	public FirstComeRankingSeq(String id, Long value, Long cts, String cby,
			Long uts, String uby) {
		this.id = id;
		this.value = value;
		this.cts = cts;
		this.cby = cby;
		this.uts = uts;
		this.uby = uby;

	}

	/**
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return value
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(Long value) {
		this.value = value;
	}

	/**
	 * 
	 * @return cts
	 */
	public Long getCts() {
		return cts;
	}

	/**
	 * 
	 * @param cts
	 */
	public void setCts(Long cts) {
		this.cts = cts;
	}

	/**
	 * 
	 * @return cby
	 */
	public String getCby() {
		return cby;
	}

	/**
	 * 
	 * @param cby
	 */
	public void setCby(String cby) {
		this.cby = cby;
	}

	/**
	 * 
	 * @return uts
	 */
	public Long getUts() {
		return uts;
	}

	/**
	 * 
	 * @param uts
	 */
	public void setUts(Long uts) {
		this.uts = uts;
	}

	/**
	 * 
	 * @return uby
	 */
	public String getUby() {
		return uby;
	}

	/**
	 * 
	 * @param uby
	 */
	public void setUby(String uby) {
		this.uby = uby;
	}

}
