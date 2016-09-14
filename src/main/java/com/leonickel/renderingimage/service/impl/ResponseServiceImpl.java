package com.leonickel.renderingimage.service.impl;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.leonickel.renderingimage.model.Still;
import com.leonickel.renderingimage.model.VideoDetails;
import com.leonickel.renderingimage.service.ResponseService;

@Singleton
public class ResponseServiceImpl implements ResponseService {
	
	private Logger logger = LoggerFactory.getLogger(ResponseServiceImpl.class);

	@Override
	public byte[] getResponse(VideoDetails videoDetails, String heightDimension) throws Exception {
		if(heightDimension == null || heightDimension.isEmpty()) {
			return getDefaultImage(videoDetails);
		}
		for(Still still : videoDetails.getStills()) {
			if(still.getUrl().contains(getHeighDimensionFormat(heightDimension))) {
				logger.info("returning filtered image from still, heighDimension: [{}], image uri: [{}]", heightDimension, still.getUrl());
				return IOUtils.toByteArray(new URL("http:" + still.getUrl()));
			}
		}
		
//		byte[] image1 = IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[0].getUrl()));
//		byte[] image2 = IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[1].getUrl()));
//		byte[] image3 = IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[2].getUrl()));
//		
//		System.out.println(image1.length);
//		System.out.println(image2.length);
//		System.out.println(image3.length);
//		
//		ByteBuffer bb = ByteBuffer.allocate(image1.length + image2.length + image3.length);
//		bb.put(image1);
//		bb.put(image2);
//		bb.put(image3);
		
//		byte[] aaa = Bytes.concat(image1, image2, image3);
//		System.out.println(aaa.length);
//		return bb.array();

		return getDefaultImage(videoDetails);
	}
	
	private String getHeighDimensionFormat(String heightDimension) {
		return heightDimension + "p";
	}
	
	private byte[] getDefaultImage(VideoDetails videoDetails) throws Exception {
		logger.info("returning default image from still, image uri: [{}]", videoDetails.getStills()[0].getUrl());
		return IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[0].getUrl()));
	}
}