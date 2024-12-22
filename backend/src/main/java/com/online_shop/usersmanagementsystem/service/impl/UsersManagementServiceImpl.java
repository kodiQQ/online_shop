package com.online_shop.usersmanagementsystem.service.impl;

import com.online_shop.usersmanagementsystem.dto.OrderDto;
import com.online_shop.usersmanagementsystem.dto.ProductDto;
import com.online_shop.usersmanagementsystem.dto.ReqRes;
import com.online_shop.usersmanagementsystem.entity.OrdersEntity;
import com.online_shop.usersmanagementsystem.entity.OurUsersEntity;
import com.online_shop.usersmanagementsystem.entity.ProductsEntity;
import com.online_shop.usersmanagementsystem.repository.OrdersRepo;
import com.online_shop.usersmanagementsystem.repository.ProductsRepo;
import com.online_shop.usersmanagementsystem.repository.UsersRepo;
import com.online_shop.usersmanagementsystem.service.FileStorageService;
import com.online_shop.usersmanagementsystem.service.UsersManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersManagementServiceImpl implements UsersManagementService {


    private final UsersRepo usersRepo;

    private final ProductsRepo productsRepo;

    private final OrdersRepo ordersRepo;

    private final JWTUtilsImpl jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Value("${BACKEND_URL}")
    private String BASE_URL;

    @Override
    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            OurUsersEntity ourUser = new OurUsersEntity();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsersEntity ourUsersEntityResult = usersRepo.save(ourUser);
            if (ourUsersEntityResult.getId() > 0) {
                resp.setOurUsersEntity((ourUsersEntityResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    @Override
    public ProductDto get_all_products() {
        ProductDto reqRes = new ProductDto();

        try {
            List<ProductsEntity> result = productsRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setProductsEntityList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No products found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }
    @Override
    public ProductDto get_product_by_id(int id) {
        ProductDto reqRes = new ProductDto();

        try {
            ProductsEntity result = productsRepo.findById(id);
            reqRes.setProductsEntity(result);
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    @Override
    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            OurUsersEntity users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Override
    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsersEntity> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersEntityList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    @Override
    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsersEntity usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsersEntity(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsersEntity> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }
    @Override
    public ProductDto deleteProduct(Integer productId) {
        ProductDto reqRes = new ProductDto();
        try {
            Optional<ProductsEntity> userOptional = productsRepo.findById(productId);
            if (userOptional.isPresent()) {
                productsRepo.deleteById(productId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }
    @Override
    public OrderDto delete_order(Integer orderId) {
        OrderDto reqRes = new OrderDto();
        try {
            Optional<OrdersEntity> userOptional = ordersRepo.findById(orderId);
            if (userOptional.isPresent()) {
                ordersRepo.deleteById(orderId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("order deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }
    @Override
    public ReqRes updateUser(Integer userId, OurUsersEntity updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsersEntity> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsersEntity existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsersEntity savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsersEntity(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsersEntity> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsersEntity(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

//    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file) throws IOException {
//        String imagePath = fileStorageService.storeFile(file);
//        return new ResponseEntity<>(imagePath, HttpStatus.CREATED);
//    }

    @Override
    public ProductDto add_product(String imagePath,ProductDto productDto) {
        ProductDto resp = new ProductDto();

        try {
            ProductsEntity product = ProductsEntity.builder()
                    .name(productDto.getName())
                    .category(productDto.getCategory())
                    .price(productDto.getPrice())
                    .imageUrl(BASE_URL+"/public/product/image/"+imagePath)
                    .build();

//            Products product = new Products();
//            product.setName(productDto.getName());
//            product.setCategory(productDto.getCategory());
//            product.setPrice(productDto.getPrice());
            ProductsEntity productResult = productsRepo.save(product);
            if (productResult.getId() > 0) {
                resp.setProductsEntity((productResult));
                resp.setMessage("Product succesfully added");
                resp.setStatusCode(200);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    @Override
    public OrderDto add_order(String email, OrderDto reqres) {
        OrderDto resp = new OrderDto();

        try {
            OrdersEntity order = new OrdersEntity();

            Optional<OurUsersEntity> userOptional = usersRepo.findByEmail(email);
            OurUsersEntity user = userOptional.orElse(null);
            order.setOurUser(user);

            List<Integer> Products_id_list = reqres.getProducts_id_list();
//            List<Products> all_products_list=productsRepo.findById();
            List<ProductsEntity> current_products_Entity_list = new ArrayList<ProductsEntity>();
            for (int id : Products_id_list) {
                current_products_Entity_list.add(productsRepo.findById(id));
            }

            order.setProducts(current_products_Entity_list);

            OrdersEntity orderResult = ordersRepo.save(order);
            if (orderResult.getId() > 0) {
                resp.setOrdersEntity(orderResult);
                resp.setMessage("Order succesfully added");
                resp.setStatusCode(200);

            }
            }catch(Exception e){
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
            }
            return resp;


    }

    @Override
    public OrderDto getOrdersByUserId(Integer userId) {
        OrderDto response = new OrderDto();

        try {
            // Pobieramy listę zamówień użytkownika
            List<Integer> orderIds = ordersRepo.findAll().stream()
                    .filter(order -> order.getOurUser().getId().equals(userId))
                    .map(OrdersEntity::getId)
                    .toList();

            System.out.println(orderIds);

            if (!orderIds.isEmpty()) {
                // Dodajemy listę zamówień do odpowiedzi
                response.setProducts_id_list(orderIds); // Tutaj można dodać specjalną listę DTO dla zamówień, jeśli potrzeba
                response.setMessage("Orders fetched successfully");
                response.setStatusCode(200);
            } else {
                response.setMessage("No orders found for user ID: " + userId);
                response.setStatusCode(404);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred: " + e.getMessage());
        }

        return response;
    }



    @Override
    public ProductDto getProductsByOrderId(Integer orderId) {
        ProductDto response = new ProductDto();

        try {
            Optional<OrdersEntity> orderOptional = ordersRepo.findById(orderId);
            if (orderOptional.isPresent()) {
                List<ProductsEntity> products = orderOptional.get().getProducts();
                response.setProductsEntityList(products);
                response.setMessage("Products fetched successfully for order ID: " + orderId);
                response.setStatusCode(200);
            } else {
                response.setMessage("Order not found for ID: " + orderId);
                response.setStatusCode(404);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred: " + e.getMessage());
        }

        return response;
    }
    @Override
    public int getIdByEmail(String email) {
        var user = usersRepo.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getId();
        }
        return 0;
    }




}

