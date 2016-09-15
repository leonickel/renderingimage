package com.leonickel.renderingimage.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.leonickel.renderingimage.dao.CredentialDAO;
import com.leonickel.renderingimage.dao.VideoDAO;
import com.leonickel.renderingimage.model.VideoDetailDTO;
import com.leonickel.renderingimage.service.VideoService;

@Singleton
public class VideoServiceImpl implements VideoService {

	@Inject
	private CredentialDAO credentialDAO;
	
	@Inject
	private VideoDAO videoDAO;
	
	@Override
	public VideoDetailDTO getVideo(String videoId, String timestamp) {
		return videoDAO.getVideo(videoId, timestamp, credentialDAO.getCredential().getAccessToken());
	}
}