package com.leonickel.renderingimage.util;

import com.google.inject.AbstractModule;
import com.leonickel.renderingimage.dao.CredentialDAO;
import com.leonickel.renderingimage.dao.VideoDAO;
import com.leonickel.renderingimage.dao.impl.CredentialDAOImpl;
import com.leonickel.renderingimage.dao.impl.VideoDAOImpl;
import com.leonickel.renderingimage.service.ResponseService;
import com.leonickel.renderingimage.service.VideoService;
import com.leonickel.renderingimage.service.impl.ResponseServiceImpl;
import com.leonickel.renderingimage.service.impl.VideoServiceImpl;

public class DependencyInjector extends AbstractModule {

	@Override
	protected void configure() {
		//Service and DAO classes
		bind(VideoService.class).to(VideoServiceImpl.class);
		bind(CredentialDAO.class).to(CredentialDAOImpl.class);
		bind(VideoDAO.class).to(VideoDAOImpl.class);
		
		//Response implementation
		bind(ResponseService.class).to(ResponseServiceImpl.class);
	}
}