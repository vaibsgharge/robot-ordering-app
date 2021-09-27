package de.tech26.robot.factory.controller;

import de.tech26.robot.factory.service.OrderServiceImpl;
import de.tech26.robot.factory.vo.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RobotFactoryController.class)
public class RobotFactoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderServiceImpl;

    @Test
    public void test_shouldAcceptValidOrder() throws Exception {

        String sampleRequest = "{\"components\": [\"I\",\"A\",\"D\",\"F\"] }";

        Mockito.when(orderServiceImpl.processOrder(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new OrderStatus("100", new BigDecimal("160.11"))));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                                        .post("/orders")
                                        .accept(APPLICATION_JSON)
                                        .content(sampleRequest)
                                        .contentType(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(CREATED.value(), response.getStatus());
        assertEquals("{\"order_id\":\"100\",\"total\":160.11}", response.getContentAsString());
    }

    @Test
    public void test_shouldNotAcceptInvalidOrder() throws Exception {
        String sampleRequest = "{\"components\": [] }";

        Mockito.when(orderServiceImpl.processOrder(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/orders")
                .accept(APPLICATION_JSON)
                .content(sampleRequest)
                .contentType(APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());
    }

    @Test
    public void test_shouldNotAcceptInvalidOrder_inCaseOfError() throws Exception {
        String sampleRequest = "";
        Mockito.when(orderServiceImpl.processOrder(Mockito.any(), Mockito.any())).thenThrow(new IllegalStateException(""));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/orders")
                .accept(APPLICATION_JSON)
                .content(sampleRequest)
                .contentType(APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());
        assertEquals("Unprocessable Entity", result.getResponse().getContentAsString());
    }
}
