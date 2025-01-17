package com.online_shop.usersmanagementsystem.controller;

import com.online_shop.usersmanagementsystem.dto.OrderDto;
import com.online_shop.usersmanagementsystem.dto.ProductAndNumberDto;
import com.online_shop.usersmanagementsystem.dto.ProductDto;
import com.online_shop.usersmanagementsystem.dto.ReqRes;
import com.online_shop.usersmanagementsystem.entity.OurUsersEntity;
import com.online_shop.usersmanagementsystem.service.FileStorageService;
import com.online_shop.usersmanagementsystem.service.UsersManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.online_shop.usersmanagementsystem.constant.Constant.PHOTO_DIR;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequiredArgsConstructor
public class UserManagementController {

    private final UsersManagementService usersManagementService;
    private final FileStorageService fileStorageService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> regeister(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PostMapping("/admin/add-product")
    public ResponseEntity<ProductDto> add_product(@RequestParam("file") MultipartFile file,ProductDto productDto) throws IOException{
//        System.out.println("111111");
        String imagePath = fileStorageService.storeFile(file);
//        return ResponseEntity.ok(productDto);
        return ResponseEntity.ok(usersManagementService.add_product(imagePath, productDto));
//    return ResponseEntity.ok(new ProductDto());
    }

    @GetMapping("/public/get-all-products")
    public ResponseEntity<ProductDto> get_all_product(){
        return ResponseEntity.ok(usersManagementService.get_all_products());

    }

    @GetMapping("/public/get-product-by-Id/{productId}")
    public ResponseEntity<ProductDto> get_product_by_id(@PathVariable Integer productId){
        return ResponseEntity.ok(usersManagementService.get_product_by_id(productId));

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

    @PostMapping("/admin/update-user/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody ReqRes reqres){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

//    @PutMapping("/admin/update-product/{productId}")
//    public ResponseEntity<ProductDto> add_product(@RequestParam("file") MultipartFile file,ProductDto productDto) throws IOException{
////        System.out.println("111111");
//        String imagePath = fileStorageService.storeFile(file);
////        return ResponseEntity.ok(productDto);
//        return ResponseEntity.ok(usersManagementService.add_product(imagePath, productDto));
////    return ResponseEntity.ok(new ProductDto());
//    }


    @PutMapping("/admin/update-product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestParam(value = "file", required = false) MultipartFile file,
                                                    @RequestPart("productDto") ProductDto productDto,
                                                    @PathVariable Integer productId) throws IOException{
//        System.out.println("1123213123");
//        System.out.println(productDto);
        String imagePath="";
        if(file != null){
            imagePath = fileStorageService.storeFile(file);
        }

        return ResponseEntity.ok(usersManagementService.updateProduct(productDto,productId,imagePath));
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

    @DeleteMapping("/admin/delete_order/{orderId}")
    public ResponseEntity<OrderDto> delete_order(@PathVariable Integer orderId){
        return ResponseEntity.ok(usersManagementService.delete_order(orderId));
    }




    //zwraca jsona ze szczegółami danego zamówienia
    @GetMapping("/public/order-products/{orderId}")
    public ResponseEntity<ProductAndNumberDto> getProductsAndNumbersByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(usersManagementService.getProductsAndNumbersByOrderId(orderId));
    }



    //poniżej są 2 getmapppingi które zwracają to samo
    //zwraca jsona z id zamówień danego użytkownika (rozpoznaje go na podstawie id w urlu)
    @GetMapping("/public/orders/{userId}")
    public ResponseEntity<OrderDto> getOrdersByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(usersManagementService.getOrdersByUserId(userId));
    }

//    zwraca jsona z id zamówień danego użytkownika (rozpoznaje go na podstawie tokena)
    @GetMapping("/adminuser/orders")
    public ResponseEntity<OrderDto> getOrdersByUserId2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        int userId=usersManagementService.getIdByEmail(email);
        return ResponseEntity.ok(usersManagementService.getOrdersByUserId(userId));
    }

    @GetMapping("/admin/allOrders")
    public ResponseEntity<OrderDto> getAllOrders() {
        return ResponseEntity.ok(usersManagementService.getAllOrders());
    }


    @GetMapping(path="/public/product/image/{filename}", produces=IMAGE_PNG_VALUE)
    public byte[] getPhoto(@PathVariable("filename") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIR + fileName));
    }











}