package com.leonickel.renderingimage.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonickel.renderingimage.exception.ImageBytesRetrievalException;
import com.leonickel.renderingimage.model.StillImageDTO;
import com.leonickel.renderingimage.model.VideoDetailDTO;

public class ResponseServiceImplTest {

	private ResponseServiceImpl responseService;
	
	@Before
	public void setUp() throws Exception {
		responseService = new ResponseServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
		responseService = null;
	}
	
	@Test
	public void test_valid_video_details_and_dimension() {
		final byte[] response = responseService.getImageResponse(createVideoDetailResponse(false), "360");
		Assert.assertTrue(response != null && response.length > 0);
	}
	
	@Test
	public void test_valid_video_details_and_invalid_dimension() {
		final byte[] response = responseService.getImageResponse(createVideoDetailResponse(false), "000");
		Assert.assertTrue(response != null && response.length > 0);
	}
	
	@Test(expected=ImageBytesRetrievalException.class)
	public void test_invalid_video_details() {
		final byte[] response = responseService.getImageResponse(createVideoDetailResponse(true), "000");
		Assert.assertTrue(response != null && response.length > 0);
	}
	
	@Test
	public void test_valid_video_details_without_dimension() {
		final String response = responseService.getHtmlResponse(createVideoDetailResponse(false));
		Assert.assertTrue(response != null && response.length() > 0);
	}
	
	@Test(expected=ImageBytesRetrievalException.class)
	public void test_invalid_video_details_without_dimension() {
		final String response = responseService.getHtmlResponse(createVideoDetailResponse(true));
		Assert.assertTrue(response != null && response.length() > 0);
	}
	
	private VideoDetailDTO createVideoDetailResponse(boolean invalidUrl) {
		final VideoDetailDTO videoDetail = new VideoDetailDTO();
		videoDetail.setId("1");
		videoDetail.setTitle("tittle1");

		final StillImageDTO[] stills = new StillImageDTO[2];
		stills[0] = createStillImage("108p", invalidUrl);
		stills[1] = createStillImage("360p", invalidUrl);

		videoDetail.setStills(stills);
		return videoDetail;
	}
	
	private StillImageDTO createStillImage(String dimension, boolean invalidUrl) {
		final StillImageDTO stillImage = new StillImageDTO();
		stillImage.setUrl(invalidUrl ? "//i-qa.video-cdn.net/4mCgfj9aaabRTixcftZ4hVAMG/22157/.JPEG" : "//i-qa.video-cdn.net/4mCgfj9bRTixcftZ4hVAMG/22157.20003."+dimension+".JPEG");
		return stillImage;
	}
}
