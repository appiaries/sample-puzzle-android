/*******************************************************************************
 * Copyright (c) 2014 Appiaries Corporation. All rights reserved.
 *******************************************************************************/
package com.appiaries.puzzle.jsonmodels;

/**
 * 
 * @author ntduc
 * 
 */
public class Player {
	/**
	 * User ID
	 */
	private String id;
	
	/**
	 * device key
	 */
	private String loginId;
	
	/**
	 * password
	 */
	private String password;
	
	/**
	 * email
	 */
	private String email;
	
	/**
	 * Auto-Login Flag Always set to be “true”
	 */
	private Boolean autoLogin;
	
	/**
	 * Custom Attribute (Nickname)
	 */
	private String nickname;
	
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

	public Player() {
	}
	
	public Player(String id, String loginId, String password, String email,
			Boolean autoLogin, String nickname, Long cts, String cby, Long uts,
			String uby) {
		this.id = id;
		this.autoLogin = autoLogin;
		this.email = email;
		this.loginId = loginId;
		this.nickname = nickname;
		this.cts = cts;
		this.cby = cby;
		this.uts = uts;
		this.uby = uby;
	}
	
	/**
	 * 
	 * @return Id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @param Id String
	 */
	public void setId(String Id) {
		this.id = Id;
	}
	
	/**
	 * 
	 * @return loginId string 
	 */
	public String getLoginId() {
		return loginId;
	}
	
	/**
	 * 
	 * @param loginId String 
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	/**
	 * 
	 * @return password string
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 
	 * @param password String
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * @return email String
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 
	 * @param email String
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 
	 * @return auto_login Boolean
	 */
	public Boolean getAutoLogin() {
		return autoLogin;
	}
	
	/**
	 * 
	 * @param auto_login Boolean
	 */
	public void setAutoLogin(Boolean autoLogin) {
		this.autoLogin = autoLogin;
	}
	
	/**
	 * 
	 * @return nickname String
	 */
	public String getNickname() {
		return nickname;
	}	
	
	/**
	 * 
	 * @param nickname String User’s Nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * 
	 * @return cby String Created By
	 */
	public String getCby() {
		return cby;
	}
	
	public Long getCts() {
		return cts;
	}

	public void setCts(Long cts) {
		this.cts = cts;
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
	 * @return uts Updated Timestamp
	 */
	public Long getUts() {
		return uts;
	}
	
	/**
	 * 
	 * @param uts Updated Timestamp
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
	 * @param uby String Updated By
	 */
	public void setUby(String uby) {
		this.uby = uby;
	}
}
