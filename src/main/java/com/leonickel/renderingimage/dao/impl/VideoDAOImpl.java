package com.leonickel.renderingimage.dao.impl;

import static com.leonickel.renderingimage.util.DefaultProperties.HTTP_TIMEOUT_CONNECTION;
import static com.leonickel.renderingimage.util.DefaultProperties.HTTP_TIMEOUT_READ;
import static com.leonickel.renderingimage.util.DefaultProperties.VIDEO_URL;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.leonickel.renderingimage.dao.VideoDAO;
import com.leonickel.renderingimage.exception.InvalidTimestampProviedException;
import com.leonickel.renderingimage.exception.NoVideoFoundException;
import com.leonickel.renderingimage.exception.VideoRetrievalException;
import com.leonickel.renderingimage.model.StillImageDTO;
import com.leonickel.renderingimage.model.VideoDetailDTO;
import com.leonickel.renderingimage.util.PropertyFinder;

@Singleton
public class VideoDAOImpl implements VideoDAO {

	private Gson gson = new Gson();
	private CloseableHttpClient httpClient = createHttpClient();
	private Logger logger = LoggerFactory.getLogger(VideoDAOImpl.class);
	
	@Override
	public VideoDetailDTO getVideo(String videoId, String timestamp, String authentication) {
		HttpGet method = null;
		CloseableHttpResponse response = null;
		final String url = PropertyFinder.getPropertyValue(VIDEO_URL).replace("{VIDEO_MANAGER_ID}", "5").replace("{VIDEO_ID}", videoId);
		try {
			method = createGetMethod(url, getAuthenticationHeader(authentication));
			logger.info("executing get method, uri: [{}]", method.getURI());
			response = httpClient.execute(method);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("invalid or not found video id provided, videoId: [{}]", videoId);
				throw new NoVideoFoundException("invalid or not found video id provided");
			}

			logger.info("sucessfull get method executed, uri: [{}]", method.getURI());
			final VideoDetailDTO videoDetails = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), VideoDetailDTO.class);
			return filterStillsByTimestamp(videoDetails, timestamp);
		} catch (ClientProtocolException e) {
			logger.error("http client error when trying to call video api, url: [{}]", url);
			abort(method);
			throw new VideoRetrievalException("http client error when trying to call video api");
		} catch (IOException e) {
			logger.error("unknown i/o error when trying to call video api, url: [{}]", url);
			abort(method);
			throw new VideoRetrievalException("unknown i/o error when trying to call video api");
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("error on closing CloseableHttpResponse object [{}]", e);
				}
			}
		}
	}
	
	private VideoDetailDTO filterStillsByTimestamp(VideoDetailDTO videoDetails, String timestamp) {
		final List<StillImageDTO> filteredStills = new ArrayList<StillImageDTO>();
		for(StillImageDTO still : videoDetails.getStills()) {
			final String info = still.getUrl().substring(still.getUrl().lastIndexOf("/")+1);
			if(info.split("\\.")[1].equals(timestamp)) {
				filteredStills.add(still);
			}
		}
		if(filteredStills.size() == 0) {
			logger.error("invalid timestamp provided for video, title: [{}] timestamp: [{}]", videoDetails.getTitle(), timestamp);
			throw new InvalidTimestampProviedException("invalid timestamp provided for video"); 
		}
		videoDetails.setStills(filteredStills.toArray(new StillImageDTO[filteredStills.size()]));
		return videoDetails;
	}
	
	private HttpGet createGetMethod(String uri, Map<String, String> headers) {
		final HttpGet method = new HttpGet(uri);
		for(String key : headers.keySet()) {
			method.setHeader(key, headers.get(key));
		}
		method.setConfig(createRequestConfig());
		return method;
	}
	
	private CloseableHttpClient createHttpClient() {
		return HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler(3, true)).build();
	}
	
	private RequestConfig createRequestConfig() {
		return RequestConfig.custom()
				.setSocketTimeout(Integer.parseInt(PropertyFinder.getPropertyValue(HTTP_TIMEOUT_READ)))
				.setConnectTimeout(Integer.parseInt(PropertyFinder.getPropertyValue(HTTP_TIMEOUT_CONNECTION)))
				.build();
	}
	
	private Map<String, String> getAuthenticationHeader(String authentication) {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer " + authentication);
		return map;
	}
	
	private void abort(HttpGet method) {
		if(method != null) {
			method.abort();
		}
	}

}