package com.online_shop.usersmanagementsystem.repository;

import com.online_shop.usersmanagementsystem.entity.OrdersEntity;
import com.online_shop.usersmanagementsystem.entity.ProductAndNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAndNumberRepo extends JpaRepository<ProductAndNumberEntity, Integer> {

}
