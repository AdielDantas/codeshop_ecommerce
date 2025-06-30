package com.codeshop.ecommerce.controllers;

import com.codeshop.ecommerce.dto.CategoryDTO;
import com.codeshop.ecommerce.entities.Category;
import com.codeshop.ecommerce.services.CategoryService;
import com.codeshop.ecommerce.tests.CategoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService service;

    private Category category;
    private CategoryDTO categoryDTO;
    private List<CategoryDTO> list;


    @BeforeEach
    void setUp() throws Exception {
        category = CategoryFactory.createCategory(2L, "Home products");
        categoryDTO = new CategoryDTO(category);

        list = List.of(categoryDTO);

        when(service.findAll()).thenReturn(list);
    }

    @Test
    public void findAllShouldReturnListOfCategoryDTO() throws Exception {
        mockMvc.perform(get("/categories")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name").value("Home products"));
    }
}
