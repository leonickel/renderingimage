package com.leonickel.renderingimage.dao.impl;

import static com.leonickel.renderingimage.util.DefaultProperties.HTTP_TIMEOUT_CONNECTION;
import static com.leonickel.renderingimage.util.DefaultProperties.HTTP_TIMEOUT_READ;
import static com.leonickel.renderingimage.util.DefaultProperties.VIDEO_URL;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.leonickel.renderingimage.dao.VideoDAO;
import com.leonickel.renderingimage.model.Still;
import com.leonickel.renderingimage.model.VideoDetails;
import com.leonickel.renderingimage.util.PropertyFinder;

@Singleton
public class VideoDAOImpl implements VideoDAO {

	private Gson gson = new Gson();
	private CloseableHttpClient httpClient = createHttpClient();
	
	@Override
	public VideoDetails getVideo(String videoId, String timestamp, String authentication) throws Exception {
		//https://api-qa.video-cdn.net/v1/vms/{VIDEO_MANAGER_ID}/videos/{VIDEO_ID}
		String url = PropertyFinder.getPropertyValue(VIDEO_URL).replace("{VIDEO_MANAGER_ID}", "5").replace("{VIDEO_ID}", videoId);
		final HttpGet method = createGetMethod(url, getAuthenticationHeader(authentication));
		System.out.println("about to executing get method " + method.getURI());
		final CloseableHttpResponse response = httpClient.execute(method);
		System.out.println("get method executed");
		final VideoDetails videoDetails = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), VideoDetails.class);
		response.close();
		return filterStillsByTimestamp(videoDetails, timestamp);
	}
	
	private VideoDetails filterStillsByTimestamp(VideoDetails videoDetails, String timestamp) {
		final List<Still> filteredStills = new ArrayList<Still>();
		for(Still still : videoDetails.getStills()) {
			final String info = still.getUrl().substring(still.getUrl().lastIndexOf("/")+1);
			if(info.split("\\.")[1].equals(timestamp)) {
				filteredStills.add(still);
			}
		}
		videoDetails.setStills(filteredStills.toArray(new Still[filteredStills.size()]));
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

}