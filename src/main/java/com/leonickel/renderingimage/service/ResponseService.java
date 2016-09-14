package com.leonickel.renderingimage.service;

import com.leonickel.renderingimage.model.VideoDetails;

public interface ResponseService {

	byte[] getResponse(VideoDetails videoDetails, String dimension) throws Exception;
}
