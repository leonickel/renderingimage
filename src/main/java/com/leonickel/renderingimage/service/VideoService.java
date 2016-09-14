package com.leonickel.renderingimage.service;

import com.leonickel.renderingimage.model.VideoDetails;

public interface VideoService {

	VideoDetails getVideo(String videoId, String timestamp) throws Exception;
}
