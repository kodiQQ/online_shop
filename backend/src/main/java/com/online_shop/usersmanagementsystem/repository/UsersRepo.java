package com.online_shop.usersmanagementsystem.repository;


import com.online_shop.usersmanagementsystem.entity.OurUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<OurUsersEntity, Integer> {

    Optional<OurUsersEntity> findByEmail(String email);

}