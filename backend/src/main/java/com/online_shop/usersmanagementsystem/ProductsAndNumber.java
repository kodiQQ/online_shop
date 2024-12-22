package com.online_shop.usersmanagementsystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductsAndNumber {
    public Integer productNumber;
    public Integer productId;
}
