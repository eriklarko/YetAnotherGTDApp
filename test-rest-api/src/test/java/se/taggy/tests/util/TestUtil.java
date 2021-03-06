package se.taggy.tests.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestUtil {

	private TestUtil() {
	}

    public static enum Method {
        GET, POST, UPDATE, DELETE;
    }

    public static final String ENDPOINT = "https://localhost:8080";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void sendRequest(String resource, Method method, BiConsumer<HttpResponse, JsonNode> cb) {
        sendRequest(resource, null, method, cb);
    }

    public static void sendRequest(String resource, JsonNode json, Method method, BiConsumer<HttpResponse, JsonNode> cb) {
        // TODO: Make sure we don't get ENDPOINT//resource or ENDPOINTresource. Exactly one slash
        String uri = ENDPOINT + resource;
        HttpUriRequest request;
        switch (method) {
            case POST:
                HttpPost post = new HttpPost(uri);
                if (json != null) {
                    attachJson(post, json);
                }
                request = post;
                break;
            case GET:
                request = new HttpGet(uri);
                break;
            case DELETE:
                request = new HttpDelete(uri);
                break;
            case UPDATE:
                HttpPut put = new HttpPut(uri);
                if (json != null) {
                    attachJson(put, json);
                }
                request = put;
                break;
            default:
                throw new IllegalArgumentException("Unsupported method " + method);
        }
        sendRequest(request, cb);
    }

	/**
	 * BLOCKING
	 */
    public static void sendRequest(HttpUriRequest request, BiConsumer<HttpResponse, JsonNode> cb) {
        try (CloseableHttpClient client = getHttpClient()) {
            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();

                String body = EntityUtils.toString(entity);
                try {
                    JsonNode jsonNode = mapper.createObjectNode();
                    if (!body.isEmpty()) {
                        jsonNode = mapper.readTree(body);
                    }
                    cb.accept(response, jsonNode);
                } catch (JsonProcessingException e) {
                    Assert.fail("Response was not valid JSON\n" + body);
                }

                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CloseableHttpClient getHttpClient() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void attachJson(HttpEntityEnclosingRequest request, JsonNode json) {
        try {
            StringEntity input = new StringEntity(json.toString());
            input.setContentType("application/json");
            request.setEntity(input);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

	public static void get(String resource, JsonNode json, BiConsumer<HttpResponse, JsonNode> cb) {
        sendRequest(resource, json, Method.GET, cb);
    }

    public static void put(String resource, JsonNode json, BiConsumer<HttpResponse, JsonNode> cb) {
        sendRequest(resource, json, Method.UPDATE, cb);
    }

    public static void post(String resource, ObjectNode json, BiConsumer<HttpResponse, JsonNode> cb) {
        sendRequest(resource, json, Method.POST, cb);
    }

	public static void delete(String resource, BiConsumer<HttpResponse, JsonNode> cb) {
        sendRequest(resource, null, Method.DELETE, cb);
    }
}
