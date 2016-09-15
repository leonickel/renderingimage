package com.leonickel.renderingimage.util;

public enum DefaultProperties {
	
	HTTP_TIMEOUT_CONNECTION("movingimage.http.timeout.connection", "5000"),
	HTTP_TIMEOUT_READ("movingimage.http.timeout.read", "5000"),
	
	CREDENTIAL_URL("movingimage.credential.url", "https://api-qa.video-cdn.net/v1/vms/auth/login"),
	CREDENTIAL_URL_USERNAME("movingimage.credential.url.username", "Joing1930@superrito.com"),
	CREDENTIAL_URL_PASSWORD("movingimage.credential.url.password", "2JmLUGu"),
	
	VIDEO_MANAGER_ID("movingimage.video.manager.id", "5"),
	VIDEO_URL("movingimage.credential.url", "https://api-qa.video-cdn.net/v1/vms/{VIDEO_MANAGER_ID}/videos/{VIDEO_ID}"),
	
	IMAGE_CONTENT_TYPE("movingimage.image.content-type", "image/jpeg"),
	HTML_CONTENT_TYPE("movingimage.html.content-type", "text/html"),
	ERROR_CONTENT_TYPE("movingimage.error.content-type", "text/plain"),
	;
	
	private String property;
	private String defaultValue;
	
	private DefaultProperties(String property, String defaultValue) {
		this.property = property;
		this.defaultValue = defaultValue;
	}
	
	public String property() {
		return property;
	}
	
	public String defaultValue() {
		return defaultValue;
	}
}