package com.codeshop.ecommerce.tests;

import com.codeshop.ecommerce.dto.ProductDTO;
import com.codeshop.ecommerce.entities.Category;
import com.codeshop.ecommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Category category = CategoryFactory.createCategory();
        Product product = new Product(1L, "PlayStation 5", "consectetur adipiscing elit, sed", 3999.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
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
        product.setId(null);
        return new ProductDTO(product);
    }
}
