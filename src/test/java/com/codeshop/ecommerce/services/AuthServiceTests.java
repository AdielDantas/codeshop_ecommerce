package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.entities.User;
import com.codeshop.ecommerce.services.exception.ForbiddenException;
import com.codeshop.ecommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserService userService;

    private User admin;
    private User selfClient;
    private User otherClient;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(1L, "Bob");
        otherClient = UserFactory.createCustomClientUser(2L, "Ana");
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenAdminLogged() throws Exception {
        when(userService.authenticated()).thenReturn(admin);
        Long userId = admin.getId();

        assertDoesNotThrow(() -> {
            service.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() throws Exception {
        when(userService.authenticated()).thenReturn(selfClient);
        Long userId = selfClient.getId();

        assertDoesNotThrow(() -> {
            service.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminThrowsForbiddenExceptionWhenClientOtherLogged() {
        when(userService.authenticated()).thenReturn(selfClient);
        Long userId = otherClient.getId();

        assertThrows(ForbiddenException.class, () -> {
            service.validateSelfOrAdmin(userId);
        });
    }
}
