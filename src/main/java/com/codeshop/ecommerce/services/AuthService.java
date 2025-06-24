package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.entities.User;
import com.codeshop.ecommerce.services.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public void validateSelfOrAdmin(Long userId) {
        User me = userService.authenticated();
        if (me.hasRole("ROLE_ADMIN")) {
            return;
        }
        if(!me.getId().equals(userId)) {
            throw new ForbiddenException("Accesso negado");
        }
    }
}
