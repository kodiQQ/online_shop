package com.online_shop.usersmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.online_shop.usersmanagementsystem.entity.Orders;
import com.online_shop.usersmanagementsystem.entity.OurUsers;
import com.online_shop.usersmanagementsystem.entity.Products;
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
    private OurUsers user_id;
    private List<Integer> products_id_list;
    private Orders orders;
    private List<Orders> OrdersList;
}
