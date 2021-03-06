package com.leonickel.renderingimage.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.leonickel.renderingimage.exception.ImageBytesRetrievalException;
import com.leonickel.renderingimage.model.StillImageDTO;
import com.leonickel.renderingimage.model.VideoDetailDTO;
import com.leonickel.renderingimage.service.ResponseService;

@Singleton
public class ResponseServiceImpl implements ResponseService {

	private Logger logger = LoggerFactory.getLogger(ResponseServiceImpl.class);

	@Override
	public byte[] getImageResponse(VideoDetailDTO videoDetails, String heightDimension) {
		try {
			for(StillImageDTO still : videoDetails.getStills()) {
				if(still.getUrl().contains(getHeighDimensionFormat(heightDimension))) {
					logger.info("returning filtered image from still, heighDimension: [{}], image uri: [{}]", heightDimension, still.getUrl());
					return IOUtils.toByteArray(new URL("http:" + still.getUrl()));
				}
			}
			return getDefaultImage(videoDetails);
		} catch (IOException e) {
			logger.error("i/o error on getting image bytes, videoId: [{}]", videoDetails.getId());
			throw new ImageBytesRetrievalException("i/o error on getting image bytes");
		}
	}
	
	@Override
	public String getHtmlResponse(VideoDetailDTO videoDetails) {
		try {
			final StringBuilder response = new StringBuilder("<html><body>");
			for(StillImageDTO still : videoDetails.getStills()) {
				response.append("<div>");
				response.append("<img id=\"aaa\" src=\"data:image/jpg;base64,");
				response.append(new String(Base64.getEncoder().encode(IOUtils.toByteArray(new URL("http:" + still.getUrl())))));
				response.append("\">");
				response.append("</div>");
			}
			response.append("</body></html>");
			return response.toString();	
		} catch (IOException e) {
			logger.error("i/o error on getting image bytes, videoId: [{}]", videoDetails.getId());
			throw new ImageBytesRetrievalException("i/o error on getting image bytes");
		}
	}

	private byte[] getDefaultImage(VideoDetailDTO videoDetails) throws IOException {
		logger.info("invalid dimension provided, returning thumbnail still image from video, image uri: [{}]", videoDetails.getStills()[0].getUrl());
		return IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[0].getUrl()));
	}

	private String getHeighDimensionFormat(String heightDimension) {
		return heightDimension + "p";
	}
}