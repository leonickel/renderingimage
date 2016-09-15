package com.leonickel.renderingimage.dao.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonickel.renderingimage.exception.InvalidTimestampProvidedException;
import com.leonickel.renderingimage.exception.NoVideoFoundException;
import com.leonickel.renderingimage.model.VideoDetailDTO;

public class VideoDAOImplTest {

	private VideoDAOImpl videoDAO;
	private CredentialDAOImpl credentialDAO;
	private String accessToken;
	
	@Before
	public void setUp() throws Exception {
		credentialDAO = new CredentialDAOImpl();
		videoDAO = new VideoDAOImpl();
		accessToken = credentialDAO.getCredential().getAccessToken();
	}

	@After
	public void tearDown() throws Exception {
		videoDAO = null;
	}
	
	@Test
	public void test_valid_video_id_and_timestamp() {
		final VideoDetailDTO videoDTO = videoDAO.getVideo("4TmXmDXZDL6keosrE79_tf", "20003", accessToken);
		Assert.assertTrue(videoDTO != null && videoDTO.getStills().length > 0);
	}
	
	@Test(expected=InvalidTimestampProvidedException.class)
	public void test_valid_video_id_and_invalid_timestamp() {
		videoDAO.getVideo("4TmXmDXZDL6keosrE79_tf", "12345", accessToken);
	}
	
	@Test(expected=NoVideoFoundException.class)
	public void test_valid_invalid_video_id() {
		videoDAO.getVideo("test_invalid", "12345", accessToken);
	}
	
}
