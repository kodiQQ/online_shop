package com.online_shop.usersmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.online_shop.usersmanagementsystem.ProductsAndNumber;
import com.online_shop.usersmanagementsystem.entity.OrdersEntity;
import com.online_shop.usersmanagementsystem.entity.OurUsersEntity;
import com.online_shop.usersmanagementsystem.entity.ProductAndNumberEntity;
import lombok.Data;

import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductAndNumberDto {

    private int statusCode;
    private String error;
    private String message;
    private List<ProductAndNumberEntity> productsAndNumbersList;
    private ProductsAndNumber productsAndNumber;


}
