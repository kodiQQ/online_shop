package com.online_shop.usersmanagementsystem.repository;

import com.online_shop.usersmanagementsystem.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepo extends JpaRepository<OrdersEntity, Integer> {
//    OurUsers findByEmail(String email);
//    Optional<OrdersEntity> findByOrderId(int orderId);
}
