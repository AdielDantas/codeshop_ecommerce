package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.dto.UserDTO;
import com.codeshop.ecommerce.entities.User;
import com.codeshop.ecommerce.projections.UserDetailsProjection;
import com.codeshop.ecommerce.repositories.UserRepository;
import com.codeshop.ecommerce.tests.UserDetailsFactory;
import com.codeshop.ecommerce.tests.UserFactory;
import com.codeshop.ecommerce.util.CustomUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private CustomUserUtil userUtil;

    private String existingUserName;
    private String nonExistingUserName;
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception {
        existingUserName = "maria@gmail.com";
        nonExistingUserName = "user@gmail.com";
        user = UserFactory.createCustomClientUser(1L, existingUserName);
        userDetails = UserDetailsFactory.createCustomAdminUser(existingUserName);

        when(repository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetails);
        when(repository.searchUserAndRolesByEmail(nonExistingUserName)).thenReturn(new ArrayList<>());

        when(repository.findByEmail(existingUserName)).thenReturn(Optional.of(user));
        when(repository.findByEmail(nonExistingUserName)).thenReturn(Optional.empty());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() throws Exception {
        UserDetails result = service.loadUserByUsername(existingUserName);
        assertNotNull(result);
        assertEquals(result.getUsername(), existingUserName);
    }

    @Test
    public void loadUserByUsernameShouldThrowUserNotFoundExceptionWhenUserDoesNotExists() throws Exception {
        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingUserName);
        });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserExists() {
        when(userUtil.getLoggedUserName()).thenReturn(existingUserName);
        User result = service.authenticated();

        assertNotNull(result);
        assertEquals(result.getUsername(), existingUserName);
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() throws Exception {
        doThrow(ClassCastException.class).when(userUtil).getLoggedUserName();

        assertThrows(UsernameNotFoundException.class, () -> {
            service.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserAuthenticated() throws Exception {
        UserService spyUserService = spy(service);
        doReturn(user).when(spyUserService).authenticated();

        UserDTO result = spyUserService.getMe();

        assertNotNull(result);
        assertEquals(result.getEmail(), existingUserName);
    }

    @Test
    public void getMeShouldThrowUsernameNotFoundExceptionWhenUserNotAuthenticated() throws Exception {
        UserService spyUserService = spy(service);

        doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();
    }
}
