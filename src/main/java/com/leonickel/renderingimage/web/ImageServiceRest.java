package com.leonickel.renderingimage.web;

import static com.leonickel.renderingimage.util.DefaultProperties.ERROR_CONTENT_TYPE;
import static com.leonickel.renderingimage.util.DefaultProperties.HTML_CONTENT_TYPE;
import static com.leonickel.renderingimage.util.DefaultProperties.IMAGE_CONTENT_TYPE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.google.inject.Inject;
import com.leonickel.renderingimage.exception.CredentialException;
import com.leonickel.renderingimage.exception.InvalidTimestampProviedException;
import com.leonickel.renderingimage.exception.NoVideoFoundException;
import com.leonickel.renderingimage.exception.VideoRetrievalException;
import com.leonickel.renderingimage.model.VideoDetailDTO;
import com.leonickel.renderingimage.service.ResponseService;
import com.leonickel.renderingimage.service.VideoService;
import com.leonickel.renderingimage.util.PropertyFinder;

@Path("/image")
public class ImageServiceRest {

	@Inject
	private VideoService videoService;
	
	@Inject
	private ResponseService responseService;
	
	final Pattern timestampPattern = Pattern.compile("\\d*");
	
	@GET
	@Path("/{videoId}/{timestamp}")
	public Response renderStillImages(@PathParam("videoId") String videoId, @PathParam("timestamp") String timestamp,
			@QueryParam("dimension") String dimension) throws Exception {
		if(!isValidTimestamp(timestamp)) {
			return writeErrorResponse("Timestamp must be a valid and existing number", HttpStatus.SC_BAD_REQUEST);
		}
		try {
			final VideoDetailDTO videoDetails = videoService.getVideo(videoId, timestamp);
			return dimension != null && !dimension.isEmpty() ? writeImageResponse(videoDetails, dimension) : writeHtmlResponse(videoDetails);
		} catch (NoVideoFoundException e) {
			return writeErrorResponse(e.getMessage(), HttpStatus.SC_NOT_FOUND);
		} catch (InvalidTimestampProviedException e) {
			return writeErrorResponse(e.getMessage(), HttpStatus.SC_BAD_REQUEST);
		} catch (CredentialException e) {
			return writeErrorResponse(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
		} catch (VideoRetrievalException e) {
			return writeErrorResponse(e.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	private Response writeImageResponse(VideoDetailDTO videoDetails, String dimension) throws Exception {
		return Response
				.status(HttpStatus.SC_OK)
				.header("Content-Type", PropertyFinder.getPropertyValue(IMAGE_CONTENT_TYPE))
				.entity(responseService.getImageResponse(videoDetails, dimension))
				.build();
	}
	
	private Response writeHtmlResponse(VideoDetailDTO videoDetails) throws Exception {
		return Response
				.status(HttpStatus.SC_OK)
				.header("Content-Type", PropertyFinder.getPropertyValue(HTML_CONTENT_TYPE))
				.entity(responseService.getHtmlResponse(videoDetails))
				.build();
	}
	
	private Response writeErrorResponse(String message, int statusCode) {
		return Response
				.status(statusCode)
				.header("Content-Type", PropertyFinder.getPropertyValue(ERROR_CONTENT_TYPE))
				.entity(message)
				.build();
	}
	
	private boolean isValidTimestamp(String timestamp) {
		final Matcher matcher = timestampPattern.matcher(timestamp);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}
}