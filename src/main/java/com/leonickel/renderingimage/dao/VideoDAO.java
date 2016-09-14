package com.leonickel.renderingimage.dao;

import com.leonickel.renderingimage.model.VideoDetails;

public interface VideoDAO {

	VideoDetails getVideo(String videoId, String timestamp, String accessToken) throws Exception;
}