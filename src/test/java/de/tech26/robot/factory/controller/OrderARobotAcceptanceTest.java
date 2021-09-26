package de.tech26.robot.factory.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderARobotAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String robotFactoryUrl;

    private ObjectMapper mapper;

    private HttpHeaders httpHeaders;

    @Before
    public void createOutputFile() {
        robotFactoryUrl = "http://localhost:" + port + "/orders";
        mapper = new ObjectMapper();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
    }

    @Test
    public void test_ShouldOrderARobot() throws Exception {

        HttpEntity<String> request = new HttpEntity<>("{\"components\":[\"I\",\"A\",\"D\",\"F\"]}", httpHeaders);
        ResponseEntity<String> responseEntityStr = testRestTemplate.postForEntity(robotFactoryUrl, request, String.class);

        assertNotNull(responseEntityStr.getBody());
        assertEquals(responseEntityStr.getStatusCode(), CREATED);
        JsonNode jsonNode = mapper.readTree(responseEntityStr.getBody());
        assertEquals("160.11", jsonNode.get("total").asText());
    }

    @Test
    public void test_shouldNotAccept_InvalidRequest() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("\"components\": \"INVALID\"", headers);

        ResponseEntity<String> responseEntityStr = testRestTemplate.postForEntity(robotFactoryUrl, request, String.class);
        assertNotNull(responseEntityStr.getBody());
        assertEquals(responseEntityStr.getStatusCode(), BAD_REQUEST);
    }

    @Test
    public void test_shouldNotAccept_InvalidConfiguration() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("{\"components\":[\"I\",\"A\",\"D\",\"D\"]}", headers);
        String roboFactoryUrl = "http://localhost:" + port + "/orders";

        ResponseEntity<String> responseEntityStr = testRestTemplate.postForEntity(roboFactoryUrl, request, String.class);
        assertNull(responseEntityStr.getBody());
        assertEquals(responseEntityStr.getStatusCode(), UNPROCESSABLE_ENTITY);
    }

    @Test
    public void test_stockExhausted() throws Exception {

        HttpEntity<String> firstOrderRequest = new HttpEntity<>("{\"components\":[\"I\",\"A\",\"D\",\"F\"]}", httpHeaders);
        ResponseEntity<String> firstOrderResponse = testRestTemplate.postForEntity(robotFactoryUrl, firstOrderRequest, String.class);

        assertNotNull(firstOrderResponse.getBody());
        assertEquals(firstOrderResponse.getStatusCode(), CREATED);
        JsonNode jsonNode = mapper.readTree(firstOrderResponse.getBody());
        assertEquals("160.11", jsonNode.get("total").asText());

        HttpEntity<String> secondOrderRequest = new HttpEntity<>("{\"components\":[\"I\",\"A\",\"D\",\"D\"]}", httpHeaders);
        ResponseEntity<String> secondOrderResponse = testRestTemplate.postForEntity(robotFactoryUrl, secondOrderRequest, String.class);

        assertNull(secondOrderResponse.getBody());
        assertEquals(secondOrderResponse.getStatusCode(), UNPROCESSABLE_ENTITY);
    }
}
