package com.leonickel.renderingimage.util;

public enum DefaultProperties {
	
	HTTP_TIMEOUT_CONNECTION("movingimage.http.timeout.connection", "5000"),
	HTTP_TIMEOUT_READ("movingimage.http.timeout.read", "5000"),
	
	CREDENTIAL_URL("movingimage.credential.url", "https://api-qa.video-cdn.net/v1/vms/auth/login"),
	CREDENTIAL_URL_USERNAME("movingimage.credential.url.username", "Joing1930@superrito.com"),
	CREDENTIAL_URL_PASSWORD("movingimage.credential.url.password", "2JmLUGu"),
	
	VIDEO_URL("movingimage.credential.url", "https://api-qa.video-cdn.net/v1/vms/{VIDEO_MANAGER_ID}/videos/{VIDEO_ID}"),
	
	IMAGE_CONTENT_TYPE("movingimage.image.content-type", "image/jpeg"),
	
//	GO_EURO_URL_MAX_RETRY("goeuro.url.max-retry", "3"),
//	
//	CSV_FILE_NAME("csv.file-name", "result.csv"),
//	CSV_OUTPUT_PATH("csv.output-path", "./"),
//	CSV_SEPARATOR_VALUE("csv.separator-value", ",")
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