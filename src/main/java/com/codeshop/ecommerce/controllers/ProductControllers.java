package com.codeshop.ecommerce.controllers;

import com.codeshop.ecommerce.dto.ProductDTO;
import com.codeshop.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products")
public class ProductControllers {

    @Autowired
    private ProductService service;

    @GetMapping(value = "/{id}")
    private ProductDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
