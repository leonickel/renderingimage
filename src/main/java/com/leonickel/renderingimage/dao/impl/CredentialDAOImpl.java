package com.leonickel.renderingimage.dao.impl;

import static com.leonickel.renderingimage.util.DefaultProperties.*;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.leonickel.renderingimage.dao.CredentialDAO;
import com.leonickel.renderingimage.model.CredentialRequest;
import com.leonickel.renderingimage.model.CredentialResponse;
import com.leonickel.renderingimage.util.PropertyFinder;
@Singleton
public class CredentialDAOImpl implements CredentialDAO {

	private Gson gson = new Gson();
	private CloseableHttpClient httpClient = createHttpClient();
	
	@Override
	public CredentialResponse getCredential() throws Exception {
		final HttpPost method = createPostMethod(PropertyFinder.getPropertyValue(CREDENTIAL_URL), getBasicHeaders());
		method.setEntity(getCredentialRequestEntity());
		CloseableHttpResponse response = httpClient.execute(method);
		return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), CredentialResponse.class);
	}
	
	private HttpEntity getCredentialRequestEntity() throws UnsupportedEncodingException {
		final CredentialRequest request = new CredentialRequest();
		request.setUsername(PropertyFinder.getPropertyValue(CREDENTIAL_URL_USERNAME));
		request.setPassword(PropertyFinder.getPropertyValue(CREDENTIAL_URL_PASSWORD));
		return new StringEntity(gson.toJson(request));
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


	private HttpPost createPostMethod(String uri, Map<String, String> headers) {
		final HttpPost method = new HttpPost(uri);
		for(String key : headers.keySet()) {
			method.setHeader(key, headers.get(key));
		}
		method.setConfig(createRequestConfig());
		return method;
	}
	
	
	private Map<String, String> getBasicHeaders() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		return map;
	}
}
