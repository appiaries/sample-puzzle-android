/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.jsonmodels;

import java.io.Serializable;

import android.R.integer;

/**
 * 
 * @author ntduc
 * 
 */
public class Stage implements Serializable, Comparable<Stage> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stage ID (Object ID)
	 */
	private String id;

	/**
	 * Stage stageId 
	 */
	private String stageName;
	
	/**
	 * Image ID
	 */
	private String imageId;

	/**
	 * Number of Horizontal Pieces
	 */
	private Integer numberOfHorizontalPieces;

	/**
	 * Number of Vertical Pieces
	 */
	private Integer numberOfVerticalPieces;

	/**
	 * Time Limit
	 */
	private Integer timeLimit;

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
	
	private int order;

	public Stage() {
	}

	public Stage(String id, String stageName, String imageId, Integer numberOfHorizontalPieces,
			Integer numberOfVerticalPieces, Integer timeLimit, Long cts,
			String cby, Long uts, String uby, int order) {
		this.id = id;
		this.stageName = stageName;
		this.imageId = imageId;
		this.numberOfHorizontalPieces = numberOfHorizontalPieces;
		this.numberOfVerticalPieces = numberOfVerticalPieces;
		this.timeLimit = timeLimit;
		this.cts = cts;
		this.cby = cby;
		this.uts = uts;
		this.uby = uby;
		this.order = order;
	}

	/**
	 * 
	 * @return String stageName
	 */
	public String getStageName() {
		return stageName;
	}

	/**
	 * 
	 * @param stageName
	 *            String stageName
	 */
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
	/**
	 * 
	 * @return stateId
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param Id
	 *            String stateId
	 */
	public void setId(String Id) {
		this.id = Id;
	}

	/**
	 * 
	 * @return imageId String Image ID
	 */
	public String getImageId() {
		return imageId;
	}

	/**
	 * 
	 * @param imageId
	 *            String Image ID
	 */
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	/**
	 * 
	 * @return timeLimit
	 */
	public Integer getTimeLimit() {
		return timeLimit;
	}

	public Integer getNumberOfHorizontalPieces() {
		return numberOfHorizontalPieces;
	}

	public void setNumberOfHorizontalPieces(Integer numberOfHorizontalPieces) {
		this.numberOfHorizontalPieces = numberOfHorizontalPieces;
	}

	public Integer getNumberOfVerticalPieces() {
		return numberOfVerticalPieces;
	}

	public void setNumberOfVerticalPieces(Integer numberOfVerticalPieces) {
		this.numberOfVerticalPieces = numberOfVerticalPieces;
	}

	/**
	 * 
	 * @param timeLimit
	 */
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * 
	 * @return cts Long Created Timestamp
	 */
	public Long getCts() {
		return cts;
	}

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
	 * @param cby
	 *            String Created By
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
	 * @param uts
	 *            Long Updated Timestamp
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
	 * @param uby
	 *            String Updated By
	 */
	public void setUby(String uby) {
		this.uby = uby;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(Stage another) {		
		int result = Integer.compare(this.getOrder(), another.getOrder());
		
		if (result == 0)
		{
			result = Long.compare(this.getOrder(), another.getOrder());
		}
		return result;
	}
	

}
