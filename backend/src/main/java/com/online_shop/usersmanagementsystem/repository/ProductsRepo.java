package com.online_shop.usersmanagementsystem.repository;

import com.online_shop.usersmanagementsystem.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepo extends JpaRepository<ProductsEntity, Integer> {
    Optional<ProductsEntity> findByName(String name);
    ProductsEntity findById(int id);
}
