package com.practise.demo.service;

import com.practise.demo.dto.UserDetailsDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDetailsDTO createUser(UserDetailsDTO userDetails);
    UserDetailsDTO getUserById(String userId);
}
