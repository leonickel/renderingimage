package com.leonickel.renderingimage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;

import com.google.gson.Gson;
import com.leonickel.renderingimage.model.CredentialRequest;
import com.leonickel.renderingimage.model.CredentialResponse;
import com.leonickel.renderingimage.model.VideoDetails;

public class Test {

	private static Map<String, String> contentTypeMap = createContentTypeMap();
	
	private static Map<String, String> createContentTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		return map;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Gson gson = new Gson();
		CloseableHttpClient httpClient = createHttpClient();
		HttpPost method = createHttpPost("https://api-qa.video-cdn.net/v1/vms/auth/login", contentTypeMap);
		method.setEntity(getUserEntity());
		CloseableHttpResponse response = httpClient.execute(method);
		final CredentialResponse credentialResponse = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), CredentialResponse.class);
		
		HttpGet getMethod = createHttpGet("https://api-qa.video-cdn.net/v1/vms/5/videos/4TmXmDXZDL6keosrE79_tf", createAuthenticationHeader(credentialResponse.getAccessToken()));
		CloseableHttpResponse response2 = httpClient.execute(getMethod);
		
		final VideoDetails videoDetails = gson.fromJson(new InputStreamReader(response2.getEntity().getContent()), VideoDetails.class);
		
		System.out.println(videoDetails.getStills()[0].getUrl());
	}
	
	private static Map<String, String> createAuthenticationHeader(String accessToken) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer " + accessToken);
		return map;
	}
	
	
	private static HttpEntity getUserEntity() throws UnsupportedEncodingException {
		Gson gson = new Gson();
		CredentialRequest request = new CredentialRequest();
		request.setUsername("Joing1930@superrito.com");
		request.setPassword("2JmLUGu");
		String value = gson.toJson(request);
		System.out.println(value);
		return new StringEntity(value);
	}
	
	private static HttpGet createHttpGet(String uri, Map<String, String> headers) {
		final HttpGet method = new HttpGet(uri);
		for(String key : headers.keySet()) {
			method.setHeader(key, headers.get(key));
		}
		method.setConfig(createRequestConfig());
		return method;
	}
	
	private static HttpPost createHttpPost(String uri, Map<String, String> headers) {
		final HttpPost method = new HttpPost(uri);
		for(String key : headers.keySet()) {
			method.setHeader(key, headers.get(key));
		}
		method.setConfig(createRequestConfig());
		return method;
	}
	
	private static CloseableHttpClient createHttpClient() {
		return HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler(3, true)).build();
	}
	
	private static RequestConfig createRequestConfig() {
		return RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
	}
}
