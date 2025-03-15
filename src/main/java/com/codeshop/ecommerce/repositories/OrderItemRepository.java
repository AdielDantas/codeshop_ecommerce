package com.codeshop.ecommerce.repositories;

import com.codeshop.ecommerce.entities.OrderItem;
import com.codeshop.ecommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
