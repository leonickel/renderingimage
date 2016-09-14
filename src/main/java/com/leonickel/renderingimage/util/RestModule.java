package com.leonickel.renderingimage.util;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.leonickel.renderingimage.web.ImageServiceRest;

public class RestModule implements Module {

	@Override
	public void configure(Binder binder) {

		//Dependency Injection creation
		binder.install(new DependencyInjector());
		
		//Rest API binds
		binder.bind(ImageServiceRest.class);
	}

}
