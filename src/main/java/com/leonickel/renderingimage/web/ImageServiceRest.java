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
	
//	@GET
//	@Path("/{videoId}/{timestamp}")
//	public Response renderStills(@PathParam("videoId") String videoId, @PathParam("timestamp") String timestamp) throws Exception {
//		Gson gson = new Gson();
//		CloseableHttpClient httpClient = createHttpClient();
//		HttpPost method = createHttpPost("https://api-qa.video-cdn.net/v1/vms/auth/login", contentTypeMap);
//		method.setEntity(getUserEntity());
//		CloseableHttpResponse response = httpClient.execute(method);
//		final CredentialResponse credentialResponse = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), CredentialResponse.class);
//		
//		HttpGet getMethod = createHttpGet("https://api-qa.video-cdn.net/v1/vms/5/videos/4TmXmDXZDL6keosrE79_tf", createAuthenticationHeader(credentialResponse.getAccessToken()));
//		CloseableHttpResponse response2 = httpClient.execute(getMethod);
//		
//		final VideoDetails videoDetails = gson.fromJson(new InputStreamReader(response2.getEntity().getContent()), VideoDetails.class);
//
//		byte[] byteArray = IOUtils.toByteArray(new URL("http:" + videoDetails.getStills()[2].getUrl()));
//		
////		BufferedImage image = ImageIO.read(new URL("http:" + videoDetails.getStills()[0].getUrl()));
////		ByteArrayOutputStream baos = new ByteArrayOutputStream();
////		ImageIO.write(image, "jpeg", baos);
////		byte[] imageInByte = baos.toByteArray();
//		return Response.status(200).entity(byteArray).header("Content-Type", "image/jpeg").build();
//	}

	
//	private static Map<String, String> contentTypeMap = createContentTypeMap();
//	
//	private static Map<String, String> createContentTypeMap() {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("Content-Type", "application/json");
//		return map;
//	}
//	
//	private Map<String, String> createAuthenticationHeader(String accessToken) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("Authorization", "Bearer " + accessToken);
//		return map;
//	}
//	
//	
//	private HttpEntity getUserEntity() throws UnsupportedEncodingException {
//		Gson gson = new Gson();
//		CredentialRequest request = new CredentialRequest();
//		request.setUsername("Joing1930@superrito.com");
//		request.setPassword("2JmLUGu");
//		String value = gson.toJson(request);
//		System.out.println(value);
//		return new StringEntity(value);
//	}
//	
//	private HttpGet createHttpGet(String uri, Map<String, String> headers) {
//		final HttpGet method = new HttpGet(uri);
//		if(headers != null) {
//			for(String key : headers.keySet()) {
//				method.setHeader(key, headers.get(key));
//			}
//		}
//		method.setConfig(createRequestConfig());
//		return method;
//	}
//	
//	private HttpPost createHttpPost(String uri, Map<String, String> headers) {
//		final HttpPost method = new HttpPost(uri);
//		for(String key : headers.keySet()) {
//			method.setHeader(key, headers.get(key));
//		}
//		method.setConfig(createRequestConfig());
//		return method;
//	}
//	
//	private CloseableHttpClient createHttpClient() {
//		return HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler(3, true)).build();
//	}
//	
//	private RequestConfig createRequestConfig() {
//		return RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
//	}
	
	
	
}
