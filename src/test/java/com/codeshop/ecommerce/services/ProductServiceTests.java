package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.dto.ProductDTO;
import com.codeshop.ecommerce.dto.ProductMinDTO;
import com.codeshop.ecommerce.entities.Product;
import com.codeshop.ecommerce.repositories.ProductRepository;
import com.codeshop.ecommerce.services.exception.DataBaseException;
import com.codeshop.ecommerce.services.exception.ResourceNotFoundException;
import com.codeshop.ecommerce.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingProductId;
    private long nonExistingProductId;
    private long dependentId;
    private Product product;
    private String productName;
    private PageImpl<Product> page;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingProductId = 1L;
        nonExistingProductId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        productName = "PlayStation 5";
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDTO();

        when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        when(repository.searchByName(any(), (Pageable)any())).thenReturn(page);

        when(repository.save(any())).thenReturn(product);

        when(repository.getReferenceById(existingProductId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        when(repository.existsById(existingProductId)).thenReturn(true);
        when(repository.existsById(nonExistingProductId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);

        doNothing().when(repository).deleteById(existingProductId);
        doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingProductId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        ProductDTO result = service.findById(existingProductId);

        assertNotNull(result);
        assertEquals(result.getId(), existingProductId);
        assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        assertThrows(ResourceNotFoundException.class, () -> {
           service.findById(nonExistingProductId);
        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDTO() throws Exception {
        Pageable pageable = PageRequest.of(0, 12);
        Page<ProductMinDTO> result = service.findAll(productName, pageable);

        assertNotNull(result);
        assertEquals(1, result.getSize());
        assertEquals(result.iterator().next().getName(), productName);
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception {
        ProductDTO result = service.insert(productDTO);
        assertNotNull(result);
        assertEquals(result.getId(), product.getId());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        ProductDTO result = service.update(existingProductId, productDTO);
        assertNotNull(result);
        assertEquals(result.getId(), existingProductId);
        assertEquals(result.getName(), productDTO.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingProductId, productDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() throws Exception {
        assertDoesNotThrow(() -> {
            service.delete(existingProductId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingProductId);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId() throws Exception {
        assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });
    }
}
