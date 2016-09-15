package com.leonickel.renderingimage.service;

import com.leonickel.renderingimage.model.VideoDetailDTO;

public interface VideoService {

	VideoDetailDTO getVideo(String videoId, String timestamp) throws Exception;
}
