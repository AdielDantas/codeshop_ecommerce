package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.dto.OrderDTO;
import com.codeshop.ecommerce.entities.Order;
import com.codeshop.ecommerce.repositories.OrderRepository;
import com.codeshop.ecommerce.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        return new OrderDTO(order);
    }
}
