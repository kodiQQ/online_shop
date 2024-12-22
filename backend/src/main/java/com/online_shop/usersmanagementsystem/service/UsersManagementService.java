package com.online_shop.usersmanagementsystem.service;

import com.online_shop.usersmanagementsystem.dto.OrderDto;
import com.online_shop.usersmanagementsystem.dto.ProductAndNumberDto;
import com.online_shop.usersmanagementsystem.dto.ProductDto;
import com.online_shop.usersmanagementsystem.dto.ReqRes;
import com.online_shop.usersmanagementsystem.entity.OurUsersEntity;

public interface UsersManagementService{
    ReqRes register(ReqRes registrationRequest);
    ReqRes login(ReqRes loginRequest);
    ProductDto get_all_products();
    ProductDto get_product_by_id(int id);
    ReqRes refreshToken(ReqRes refreshTokenReqiest);
    ReqRes getAllUsers();
    ReqRes getUsersById(Integer id);
    ReqRes deleteUser(Integer userId);
    ProductDto deleteProduct(Integer productId);
    OrderDto delete_order(Integer orderId);
    ReqRes updateUser(Integer userId, OurUsersEntity updatedUser);
    ReqRes getMyInfo(String email);
    ProductDto add_product(String imagePath,ProductDto productDto);
    OrderDto add_order(String email, OrderDto reqres);
    OrderDto getOrdersByUserId(Integer userId);
    ProductAndNumberDto getProductsAndNumbersByOrderId(Integer orderId);
    int getIdByEmail(String email);

}
