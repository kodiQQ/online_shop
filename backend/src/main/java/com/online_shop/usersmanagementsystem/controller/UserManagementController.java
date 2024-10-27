package com.online_shop.usersmanagementsystem.controller;

import com.online_shop.usersmanagementsystem.dto.OrderDto;
import com.online_shop.usersmanagementsystem.dto.ProductDto;
import com.online_shop.usersmanagementsystem.dto.ReqRes;
import com.online_shop.usersmanagementsystem.entity.OurUsers;
import com.online_shop.usersmanagementsystem.entity.Products;
import com.online_shop.usersmanagementsystem.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {
    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> regeister(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PostMapping("/admin/add-product")
    public ResponseEntity<ProductDto> add_product(@RequestBody ProductDto productDto){
        return ResponseEntity.ok(usersManagementService.add_product(productDto));
    }

    @GetMapping("/admin/get-all-products")
    public ResponseEntity<ProductDto> get_all_product(){
        return ResponseEntity.ok(usersManagementService.get_all_products());

    }


    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody OurUsers reqres){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-user/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @DeleteMapping("/admin/delete-product/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Integer productId){
        return ResponseEntity.ok(usersManagementService.deleteProduct(productId));
    }

    @PostMapping("/adminuser/add_order")
    public ResponseEntity<OrderDto> add_Order(@RequestBody OrderDto reqres){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(usersManagementService.add_order(email,reqres));
    }

    @DeleteMapping("admin/delete_order/{orderId}")
    public ResponseEntity<OrderDto> delete_order(@PathVariable Integer orderId){
        return ResponseEntity.ok(usersManagementService.delete_order(orderId));
    }










}