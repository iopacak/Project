package com.igoro;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.servlet.ServletContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileUploadServiceApplicationTests {
	
	@Test
	void contextLoads() {
	}
	
    @LocalServerPort
    private int port;
    
	@Test
	public void testUploadFiles() throws Throwable {
		
		int numberOfRequest = 100;
		
		Random random = new Random(System.currentTimeMillis());
		
        int contentLength = 1024 * 1024 * 50;
        byte[] file = new byte[contentLength];
        
        File fileUpload = new File("test");
        fileUpload.createNewFile();        
        
        Path path = Paths.get(fileUpload.toURI());
        Files.write(path, file);
        
        int timeout = 5;
        RequestConfig config = RequestConfig.custom()
          .setConnectTimeout(timeout * 1000)
          .setConnectionRequestTimeout(timeout * 1000)
          .setSocketTimeout(timeout * 1000).build();

        CloseableHttpClient client =  HttpClientBuilder.create().setDefaultRequestConfig(config).build();

//		HttpClient client = HttpClient.newBuilder()
//				.version(Version.HTTP_1_1)
//				.followRedirects(Redirect.NORMAL)
//				.connectTimeout(Duration.ofMinutes(1))
//				.build();
        
//        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//        bodyBuilder.part("file", fileUpload);
//        
//        MultiValueMap<String, HttpEntity<?>> multipartBody = bodyBuilder.build();  
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", fileUpload, ContentType.APPLICATION_OCTET_STREAM, "test.zip");
        
        org.apache.http.HttpEntity multipart = builder.build();		
		
		for (int i = 0; i < numberOfRequest; i++) {	
			
			HttpPost post = new HttpPost("http://localhost:" + port + "/api/v1/upload/");
			post.addHeader("X-Upload-File", "test" + random.nextInt(10000) + ".zip");
			post.setEntity(multipart); 
			
			CloseableHttpResponse response = client.execute(post);
			
//			HttpRequest request = HttpRequest.newBuilder()
//					.uri(URI.create("http://localhost:" + port + "/api/v1/upload/"))
//				    .timeout(Duration.ofMinutes(1))
//				    .header("X-Upload-File", "test" + random.nextInt(10000) + ".zip")
//				    .header("Content-Type", "multipart/form-data")
//				    .POST(BodyPublishers.ofString(bodyBuilder.toString()))
//				    .build();
//			
//			client.send(request, BodyHandlers.ofString());					    
			
		}		
		
	}
	

}
