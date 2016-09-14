package com.leonickel.renderingimage.model;

public class VideoDetails {
	
	private String id;
	private String title;
	private Still[] stills;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Still[] getStills() {
		return stills;
	}

	public void setStills(Still[] stills) {
		this.stills = stills;
	}

	
}
