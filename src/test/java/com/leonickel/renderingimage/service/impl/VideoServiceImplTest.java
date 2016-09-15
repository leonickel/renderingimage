package com.leonickel.renderingimage.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.leonickel.renderingimage.dao.CredentialDAO;
import com.leonickel.renderingimage.dao.VideoDAO;
import com.leonickel.renderingimage.exception.CredentialException;
import com.leonickel.renderingimage.exception.InvalidTimestampProvidedException;
import com.leonickel.renderingimage.exception.NoVideoFoundException;
import com.leonickel.renderingimage.model.CredentialResponseDTO;
import com.leonickel.renderingimage.model.StillImageDTO;
import com.leonickel.renderingimage.model.VideoDetailDTO;

public class VideoServiceImplTest {

	private VideoServiceImpl videoService;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		CredentialDAO credentialDAO = Mockito.mock(CredentialDAO.class);
		VideoDAO videoDAO = Mockito.mock(VideoDAO.class);
		
		Mockito.when(credentialDAO.getCredential()).thenReturn(createCredentialResponse());
		Mockito.when(videoDAO.getVideo("video1", "1000", "accessToken")).thenReturn(createVideoDetailResponse());
		Mockito.when(videoDAO.getVideo("invalidvideo", "1000", "accessToken")).thenThrow(NoVideoFoundException.class);
		Mockito.when(videoDAO.getVideo("video1", "12345", "accessToken")).thenThrow(InvalidTimestampProvidedException.class);

		videoService = new VideoServiceImpl();
		videoService.setCredentialDAO(credentialDAO);
		videoService.setVideoDAO(videoDAO);
	}

	@After
	public void tearDown() throws Exception {
		videoService = null;
	}

	@Test
	public void test_valid_video_id_and_timestamp() {
		final VideoDetailDTO videoDTO = videoService.getVideo("video1", "1000");
		Assert.assertTrue(videoDTO != null && videoDTO.getStills().length == 2);
	}
	
	@Test(expected=NoVideoFoundException.class)
	public void test_invalid_video_id() {
		videoService.getVideo("invalidvideo", "1000");
	}
	
	@Test(expected=InvalidTimestampProvidedException.class)
	public void test_valid_video_id_and_invalid_timestamp() {
		videoService.getVideo("video1", "12345");
	}
	
	private CredentialResponseDTO createCredentialResponse() {
		final CredentialResponseDTO response = new CredentialResponseDTO();
		response.setAccessToken("accessToken");
		response.setRefreshToken("refreshToken");
		return response;
	}
	
	private VideoDetailDTO createVideoDetailResponse() {
		final VideoDetailDTO videoDetail = new VideoDetailDTO();
		videoDetail.setId("1");
		videoDetail.setTitle("tittle1");

		final StillImageDTO[] stills = new StillImageDTO[2];
		stills[0] = createStillImage("0");
		stills[1] = createStillImage("1");

		videoDetail.setStills(stills);
		return videoDetail;
	}
	
	private StillImageDTO createStillImage(String id) {
		final StillImageDTO stillImage = new StillImageDTO();
		stillImage.setUrl("url"+id);
		return stillImage;
	}
	
}
