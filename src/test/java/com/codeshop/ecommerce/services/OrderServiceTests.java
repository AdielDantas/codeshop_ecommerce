package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.dto.OrderDTO;
import com.codeshop.ecommerce.entities.Order;
import com.codeshop.ecommerce.entities.OrderItem;
import com.codeshop.ecommerce.entities.Product;
import com.codeshop.ecommerce.entities.User;
import com.codeshop.ecommerce.repositories.OrderItemRepository;
import com.codeshop.ecommerce.repositories.OrderRepository;
import com.codeshop.ecommerce.repositories.ProductRepository;
import com.codeshop.ecommerce.services.exception.ForbiddenException;
import com.codeshop.ecommerce.services.exception.ResourceNotFoundException;
import com.codeshop.ecommerce.tests.OrderFactory;
import com.codeshop.ecommerce.tests.ProductFactory;
import com.codeshop.ecommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private long existingOrderId;
    private long nonExistingOrderId;
    private long existingProductId;
    private long nonExistingProductId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin;
    private User client;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;
        existingProductId = 1L;
        nonExistingProductId = 2L;
        admin = UserFactory.createCustomAdminUser(1L, "Jef");
        client = UserFactory.createCustomClientUser(2L, "Bob");
        order = OrderFactory.createOrder(client);
        orderDTO = new OrderDTO();
        product = ProductFactory.createProduct();

        when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
        when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        when(repository.save(any())).thenReturn(order);
        when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() throws Exception {
        doNothing().when(authService).validateSelfOrAdmin(any());
        OrderDTO result = service.findById(existingOrderId);

        assertNotNull(result);
        assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() throws Exception {
        doNothing().when(authService).validateSelfOrAdmin(any());
        OrderDTO result = service.findById(existingOrderId);

        assertNotNull(result);
        assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

        assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = service.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldThrowsForbiddenExceptionWhenIdDoesNotExists() {
        doNothing().when(authService).validateSelfOrAdmin(any());

        assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = service.findById(nonExistingOrderId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenAdminLogged() {
        when(userService.authenticated()).thenReturn(admin);
        OrderDTO result = service.insert(orderDTO);

        assertNotNull(result);
    }

    @Test
    public void insertShouldReturnOrderDTOWhenClientLogged() {
        when(userService.authenticated()).thenReturn(admin);
        OrderDTO result = service.insert(orderDTO);

        assertNotNull(result);
    }

    @Test
    public void insertShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {
        doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        assertThrows(UsernameNotFoundException.class, () -> {
            OrderDTO result = service.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductIdDoesNotExists() {
        when(userService.authenticated()).thenReturn(admin);

        product.setId(nonExistingOrderId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);

        assertThrows(EntityNotFoundException.class, () -> {
            OrderDTO result = service.insert(orderDTO);
        });
    }
}
