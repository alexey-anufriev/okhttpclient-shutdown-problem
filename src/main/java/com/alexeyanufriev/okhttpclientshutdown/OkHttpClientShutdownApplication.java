package com.alexeyanufriev.okhttpclientshutdown;

import okhttp3.OkHttpClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

@SpringBootApplication
public class OkHttpClientShutdownApplication implements ApplicationRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(OkHttpClientShutdownApplication.class).web(WebApplicationType.NONE).run(args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
		CustomExternalRestTemplate restTemplate = new CustomExternalRestTemplate(requestFactory);

		ResponseEntity<String> result = restTemplate.exchange("https://google.com", HttpMethod.GET, null, String.class);
		System.out.println(result.getStatusCode());

		requestFactory.destroy();

		// To be able to terminate the app properly this line must be uncommented.
		// evictConnectionPool(requestFactory);

		System.out.println("finish");
	}

	private void evictConnectionPool(OkHttp3ClientHttpRequestFactory requestFactory) throws Exception {
		Field clientField = OkHttp3ClientHttpRequestFactory.class.getDeclaredField("client");
		clientField.setAccessible(true);
		OkHttpClient client = (OkHttpClient) clientField.get(requestFactory);
		client.connectionPool().evictAll();
	}

	public class CustomExternalRestTemplate extends RestTemplate {

		public CustomExternalRestTemplate(OkHttp3ClientHttpRequestFactory requestFactory) {
			super(requestFactory);
		}
	}

}
