package com.leonickel.renderingimage.dao;

import com.leonickel.renderingimage.model.VideoDetailDTO;

public interface VideoDAO {

	VideoDetailDTO getVideo(String videoId, String timestamp, String accessToken) throws Exception;
}