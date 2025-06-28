package com.codeshop.ecommerce.tests;

import com.codeshop.ecommerce.entities.Category;

public class CategoryFactory {

        public static Category createCategory() {
            return new Category(2L, "Eletrônicos");
        }

        public static Category createCategory(Long id, String name) {
            return new Category(id, name);
        }
}
