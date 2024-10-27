package com.online_shop.usersmanagementsystem.repository;

import com.online_shop.usersmanagementsystem.entity.Orders;
import com.online_shop.usersmanagementsystem.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepo extends JpaRepository<Orders, Integer> {
//    OurUsers findByEmail(String email);
}
