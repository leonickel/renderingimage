package com.leonickel.renderingimage.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leonickel.renderingimage.dao.CredentialDAO;
import com.leonickel.renderingimage.dao.VideoDAO;
import com.leonickel.renderingimage.model.VideoDetails;
import com.leonickel.renderingimage.service.VideoService;

@Singleton
public class VideoServiceImpl implements VideoService {

	@Inject
	private CredentialDAO credentialDAO;
	
	@Inject
	private VideoDAO videoDAO;
	
	@Override
	public VideoDetails getVideo(String videoId, String timestamp) throws Exception {
		return videoDAO.getVideo(videoId, timestamp, credentialDAO.getCredential().getAccessToken());
	}
}