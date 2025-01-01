package com.practise.demo.service;

import com.practise.demo.dao.UserDetailsDao;
import com.practise.demo.dto.UserDetailsDTO;
import com.practise.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    private UserDetailsDao userDetailsDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsDTO createUser(UserDetailsDTO userDetails){
        UserEntity userEntity = createUserEntityFromUserDetails(userDetails);
        userDetailsDao.createNewUser(userEntity);
        userDetails.setUserId(userEntity.getUserId());
        return userDetails;
    }

    public UserDetailsDTO getUserById(String userId){
        UserEntity userById = userDetailsDao.getUserById(userId);
        if(nonNull(userById)){
            return createUserDetailsFromUserEntity(userById);
        }
        return null;
    }

    public Collection<UserDetailsDTO> getAllUsers(){
        Collection<UserEntity> allUsers = userDetailsDao.getAllUsers();
        Collection<UserDetailsDTO> userDetails = Collections.emptyList();
        if(nonNull(allUsers) && !allUsers.isEmpty()){
            userDetails = allUsers.stream()
                    .map(userEntity -> createUserDetailsFromUserEntity(userEntity))
                    .collect(Collectors.toList());
        }
        return userDetails;
    }

    public UserDetailsDTO updateUsers(UserDetailsDTO userDetails, String userId){
        UserDetailsDTO userById = getUserById(userId);
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

    private UserEntity createUserEntityFromUserDetails(UserDetailsDTO userDetails){
        UserEntity userEntity = UserEntity.builder()
                .userId(userDetails.getEmailId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .emailId(userDetails.getEmailId())
                .password(bCryptPasswordEncoder.encode(userDetails.getPassword()))
                .userType(userDetails.getUserType())
                .build();
        return userEntity;
    }

    private UserDetailsDTO createUserDetailsFromUserEntity(UserEntity userEntity){
        UserDetailsDTO userDetails = UserDetailsDTO.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .emailId(userEntity.getEmailId())
                .password(userEntity.getPassword())
                .userType(userEntity.getUserType())
                .build();
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsDTO userById = getUserById(username);
        if(isNull(userById))
            throw new UsernameNotFoundException(username);
        return new User(userById.getUserId(),userById.getPassword(),new ArrayList<>());
    }
}
