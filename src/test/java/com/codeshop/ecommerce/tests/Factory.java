package com.codeshop.ecommerce.tests;

import com.codeshop.ecommerce.dto.ProductDTO;
import com.codeshop.ecommerce.entities.Category;
import com.codeshop.ecommerce.entities.Product;

public class Factory {

    public static Product createProduct() {
        Category category = createCategory();
        Product product = new Product(1L, "PlayStation 5", "Consectetur adipiscing elit, sed ", 3999.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/17-big.jpg");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name) {
        Product product = createProduct();
        product.setName(name);
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }

    public static Category createCategory() {
        return new Category(1L, "Games");
    }

    public static Category createCategory(Long id, String name) {
        return new Category(id, name);
    }
}
