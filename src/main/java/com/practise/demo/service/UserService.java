package com.practise.demo.service;

import com.practise.demo.dao.UserDetailsDao;
import com.practise.demo.dto.UserDetails;
import com.practise.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class UserService {

    @Autowired
    private UserDetailsDao userDetailsDao;

    public UserDetails createUser(UserDetails userDetails){
        UserEntity userEntity = createUserEntityFromUserDetails(userDetails);
        userDetailsDao.createNewUser(userEntity);
        userDetails.setUserId(userEntity.getUserId());
        return userDetails;
    }

    public UserDetails getUserById(String userId){
        UserEntity userById = userDetailsDao.getUserById(userId);
        if(nonNull(userById)){
            return createUserDetailsFromUserEntity(userById);
        }
        return null;
    }

    public Collection<UserDetails> getAllUsers(){
        Collection<UserEntity> allUsers = userDetailsDao.getAllUsers();
        Collection<UserDetails> userDetails = Collections.emptyList();
        if(nonNull(allUsers) && !allUsers.isEmpty()){
            userDetails = allUsers.stream()
                    .map(userEntity -> createUserDetailsFromUserEntity(userEntity))
                    .collect(Collectors.toList());
        }
        return userDetails;
    }

    public UserDetails updateUsers(UserDetails userDetails,String userId){
        UserDetails userById = getUserById(userId);
        if(nonNull(userById)){
            userById.setFirstName(userDetails.getFirstName());
            userById.setLastName(userDetails.getLastName());
            userById.setEmailId(userDetails.getEmailId());
            UserEntity userEntity = createUserEntityFromUserDetails(userById);
            userDetailsDao.updateUser(userEntity,userId);
            return userById;
        }
        return null;
    }

    public boolean deleteUserById(String userId){
        return userDetailsDao.removeUserById(userId);
    }

    private UserEntity createUserEntityFromUserDetails(UserDetails userDetails){
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .emailId(userDetails.getEmailId())
                .build();
        return userEntity;
    }

    private UserDetails createUserDetailsFromUserEntity(UserEntity userEntity){
        UserDetails userDetails = UserDetails.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .emailId(userEntity.getEmailId())
                .build();
        return userDetails;
    }
}
