package com.leonickel.renderingimage.service;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
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

@Path("/image")
public class ImageService {

	@GET
	@Path("/{videoId}")
	public Response printMessage(@PathParam("videoId") String videoId) throws Exception {
		Gson gson = new Gson();
		CloseableHttpClient httpClient = createHttpClient();
		HttpPost method = createHttpPost("https://api-qa.video-cdn.net/v1/vms/auth/login", contentTypeMap);
		method.setEntity(getUserEntity());
		CloseableHttpResponse response = httpClient.execute(method);
		final CredentialResponse credentialResponse = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), CredentialResponse.class);
		
		HttpGet getMethod = createHttpGet("https://api-qa.video-cdn.net/v1/vms/5/videos/4TmXmDXZDL6keosrE79_tf", createAuthenticationHeader(credentialResponse.getAccessToken()));
		CloseableHttpResponse response2 = httpClient.execute(getMethod);
		
		final VideoDetails videoDetails = gson.fromJson(new InputStreamReader(response2.getEntity().getContent()), VideoDetails.class);

		byte[] byteArray = IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[2].getUrl()));
		
//		BufferedImage image = ImageIO.read(new URL("http:" + videoDetails.getStills()[0].getUrl()));
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(image, "jpeg", baos);
//		byte[] imageInByte = baos.toByteArray();
		return Response.status(200).entity(byteArray).header("Content-Type", "image/jpeg").build();
	}
	
	private static Map<String, String> contentTypeMap = createContentTypeMap();
	
	private static Map<String, String> createContentTypeMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		return map;
	}
	
	private Map<String, String> createAuthenticationHeader(String accessToken) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer " + accessToken);
		return map;
	}
	
	
	private HttpEntity getUserEntity() throws UnsupportedEncodingException {
		Gson gson = new Gson();
		CredentialRequest request = new CredentialRequest();
		request.setUsername("Joing1930@superrito.com");
		request.setPassword("2JmLUGu");
		String value = gson.toJson(request);
		System.out.println(value);
		return new StringEntity(value);
	}
	
	private HttpGet createHttpGet(String uri, Map<String, String> headers) {
		final HttpGet method = new HttpGet(uri);
		if(headers != null) {
			for(String key : headers.keySet()) {
				method.setHeader(key, headers.get(key));
			}
		}
		method.setConfig(createRequestConfig());
		return method;
	}
	
	private HttpPost createHttpPost(String uri, Map<String, String> headers) {
		final HttpPost method = new HttpPost(uri);
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
		return RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
	}
	
	
	
}
