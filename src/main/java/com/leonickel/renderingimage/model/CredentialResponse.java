package com.leonickel.renderingimage.model;

public class CredentialResponse {

	private String accessToken;
	private String refreshToken;
	private int validForVideoManager;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public int getValidForVideoManager() {
		return validForVideoManager;
	}
	public void setValidForVideoManager(int validForVideoManager) {
		this.validForVideoManager = validForVideoManager;
	}
	
	
}
