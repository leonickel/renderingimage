package com.leonickel.renderingimage.model;

public class VideoDetailDTO {
	
	private String id;
	private String title;
	private StillImageDTO[] stills;
	
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

	public StillImageDTO[] getStills() {
		return stills;
	}

	public void setStills(StillImageDTO[] stills) {
		this.stills = stills;
	}
}