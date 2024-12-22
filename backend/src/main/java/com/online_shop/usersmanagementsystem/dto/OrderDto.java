package com.online_shop.usersmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.online_shop.usersmanagementsystem.entity.OrdersEntity;
import com.online_shop.usersmanagementsystem.entity.OurUsersEntity;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private OurUsersEntity user_id;
    private List<Integer> products_id_list;
    private OrdersEntity ordersEntity;
    private List<OrdersEntity> ordersEntityList;
}
