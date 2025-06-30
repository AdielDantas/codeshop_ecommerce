package com.codeshop.ecommerce.controllers.it;

import com.codeshop.ecommerce.dto.OrderDTO;
import com.codeshop.ecommerce.entities.Order;
import com.codeshop.ecommerce.entities.OrderItem;
import com.codeshop.ecommerce.entities.Product;
import com.codeshop.ecommerce.entities.User;
import com.codeshop.ecommerce.tests.OrderFactory;
import com.codeshop.ecommerce.tests.ProductFactory;
import com.codeshop.ecommerce.tests.TokenUtil;
import com.codeshop.ecommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingOrderId;
    private Long nonExistingOrderId;

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String clientToken, adminToken, invalidToken;

    private Order order;
    private OrderDTO orderDTO;
    private User adminUser;
    private User clientUser;

    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 1000L;

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "xpto";

        adminUser = UserFactory.createAdminUser();
        clientUser = UserFactory.createClientUser();
        order = OrderFactory.createOrder(clientUser);
        orderDTO = new OrderDTO();

        product = ProductFactory.createProduct();

        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {
        mockMvc.perform(get("/orders/{id}", existingOrderId)
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingOrderId))
                .andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.client.name").value("Maria Brown"))
                .andExpect(jsonPath("$.payment").exists())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[1].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() throws Exception {
        mockMvc.perform(get("/orders/{id}", existingOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingOrderId))
                .andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.client.name").value("Maria Brown"))
                .andExpect(jsonPath("$.payment").exists())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[1].name").value("Macbook Pro"))
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    public void findByIdShouldReturnForbbidenWhenIdExistsAndClientLoggedAndOrderBelongUser() throws Exception {

        Long otherOrderId = 2L;

        mockMvc.perform(get("/orders/{id}", otherOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistsAndAdminLogged() throws Exception {
        mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExistsAndClientLogged() throws Exception {
        mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
                        .header("Authorization", "Bearer " + invalidToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
