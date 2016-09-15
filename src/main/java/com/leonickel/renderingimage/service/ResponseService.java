package com.leonickel.renderingimage.service;

import com.leonickel.renderingimage.model.VideoDetailDTO;

public interface ResponseService {

	byte[] getImageResponse(VideoDetailDTO videoDetails, String dimension) throws Exception;
	
	String getHtmlResponse(VideoDetailDTO videoDetails) throws Exception;
}
