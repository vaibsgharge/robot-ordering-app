package de.tech26.robot.factory.controller;

import de.tech26.robot.factory.service.OrderServiceImpl;
import de.tech26.robot.factory.vo.OrderStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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

@RunWith(SpringRunner.class)
@WebMvcTest(value = RobotFactoryController.class)
public class RobotFactoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderServiceImpl;

    private String sampleRequest = "{\"components\": [\"I\",\"A\",\"D\",\"F\"] }";

    @Test
    public void test_shouldAcceptValidOrder() throws Exception {

        Mockito.when(orderServiceImpl.processOrder(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new OrderStatus("100", new BigDecimal("160.11"))));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                                        .post("/orders")
                                        .accept(APPLICATION_JSON)
                                        .content(sampleRequest)
                                        .contentType(APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("{\"order_id\":\"100\",\"total\":160.11}", response.getContentAsString());
    }
}
