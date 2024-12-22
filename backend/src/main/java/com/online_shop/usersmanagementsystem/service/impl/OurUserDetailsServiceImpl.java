package com.online_shop.usersmanagementsystem.service.impl;


import com.online_shop.usersmanagementsystem.repository.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OurUserDetailsServiceImpl implements UserDetailsService {


    private final UsersRepo usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepo.findByEmail(username).orElseThrow();
    }
}