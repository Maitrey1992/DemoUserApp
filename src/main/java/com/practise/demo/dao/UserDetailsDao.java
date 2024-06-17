package com.practise.demo.dao;

import com.practise.demo.dto.UserDetails;
import com.practise.demo.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
public class UserDetailsDao {

    private Map<String, UserEntity> userDetailsMap = new HashMap();
    public UserEntity getUserById(int id){
        return userDetailsMap.get(id);
    }

    public UserEntity createNewUser(UserEntity userEntity) {
        userDetailsMap.put(userEntity.getUserId(),userEntity);
        return userEntity;
    }

    public void updateUser(UserEntity userEntity,String userId){
        if(userDetailsMap.containsKey(userId)){
            userDetailsMap.put(userId,userEntity);
        }
    }

    public UserEntity getUserById(String userId){
        return userDetailsMap.get(userId);
    }

    public Collection<UserEntity> getAllUsers(){
        return userDetailsMap.values();
    }

    public boolean removeUserById(String userId){
        return nonNull(userDetailsMap.remove(userId));
    }
}
