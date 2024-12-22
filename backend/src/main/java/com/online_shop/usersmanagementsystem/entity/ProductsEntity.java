package com.online_shop.usersmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String category;
    private String price;
    private String imageUrl;
}
