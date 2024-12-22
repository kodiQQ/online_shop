package com.online_shop.usersmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relacja wiele zamówień do jednego użytkownika (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private OurUsersEntity ourUser;

    // Relacja wiele zamówień do wielu produktów (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductsEntity> products;
}
