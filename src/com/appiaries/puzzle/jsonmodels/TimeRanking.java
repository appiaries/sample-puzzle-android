/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.jsonmodels;


/**
 * 
 * @author ntduc
 * 
 */
public class TimeRanking implements Comparable<TimeRanking> {

	/**
	 * Time Ranking ID
	 */
	private String id;

	/**
	 * Stage ID
	 */
	private String stageId;

	/**
	 * Player (user) ID
	 */
	private String userId;
	
	/**
	 * nickname
	 */
	private String nickname;
	/**
	 * score
	 */
	private int score;

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
	public TimeRanking() {
	}

	/**
	 * 
	 * @param id
	 * @param stageId
	 * @param userId
	 * @param rank
	 * @param score
	 * @param cts
	 * @param cby
	 * @param uts
	 * @param uby
	 */
	public TimeRanking(String id, String stageId, String userId, String nickname,int score,
			Long cts, String cby, Long uts, String uby) {
		this.id = id;
		this.stageId = stageId;		
		this.userId = userId;
		this.nickname = nickname;
		this.score = score;
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
	 * @return stageId
	 */
	public String getStageId() {
		return stageId;
	}

	/**
	 * 
	 * @param stageId
	 */
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	/**
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickName) {
		this.nickname = nickName;
	}

	/**
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * 
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
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

	@Override
	public int compareTo(TimeRanking another) {
		int result = Integer.compare(this.getScore(), another.getScore());
		
		if (result == 0)
		{
			result = Long.compare(this.getCts(), another.getCts());
		}
		
		return result;
	}

}
