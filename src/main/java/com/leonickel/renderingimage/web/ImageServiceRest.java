package com.leonickel.renderingimage.web;

import static com.leonickel.renderingimage.util.DefaultProperties.IMAGE_CONTENT_TYPE;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.google.inject.Inject;
import com.leonickel.renderingimage.model.VideoDetails;
import com.leonickel.renderingimage.service.ResponseService;
import com.leonickel.renderingimage.service.VideoService;
import com.leonickel.renderingimage.util.PropertyFinder;

@Path("/image")
public class ImageServiceRest {

	@Inject
	private VideoService videoService;
	
	@Inject
	private ResponseService responseService;
	
	@GET
	@Path("/{videoId}/{timestamp}")
	public Response renderStills(@PathParam("videoId") String videoId, @PathParam("timestamp") String timestamp,
			@QueryParam("dimension") String dimension) throws Exception {
		final VideoDetails videoDetails = videoService.getVideo(videoId, timestamp);
		return Response.status(HttpStatus.SC_OK)
				.header("Content-Type", PropertyFinder.getPropertyValue(IMAGE_CONTENT_TYPE))
				.entity(responseService.getResponse(videoDetails, dimension)).build();
	}
}