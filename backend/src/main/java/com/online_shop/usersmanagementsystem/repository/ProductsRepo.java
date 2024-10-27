package com.online_shop.usersmanagementsystem.repository;

import com.online_shop.usersmanagementsystem.entity.OurUsers;
import com.online_shop.usersmanagementsystem.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepo extends JpaRepository<Products, Integer> {
    Optional<Products> findByName(String name);
    Products findById(int id);
}
