package com.codeshop.ecommerce.services;

import com.codeshop.ecommerce.dto.CategoryDTO;
import com.codeshop.ecommerce.entities.Category;
import com.codeshop.ecommerce.repositories.CategoryRepository;
import com.codeshop.ecommerce.tests.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private Category category;
    private List<Category> list;

    @BeforeEach
    void setUp() throws Exception {
        category = CategoryFactory.createCategory();

        list = new ArrayList<>();
        list.add(category);

        when(repository.findAll()).thenReturn(list);
    }

    @Test
    public void findAllShouldReturnListCategoryDTO() throws Exception {
        List<CategoryDTO> result = service.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(result.get(0).getId(), category.getId());
        Assertions.assertEquals(result.get(0).getName(), category.getName());
    }
}
