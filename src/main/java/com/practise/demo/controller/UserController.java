package com.practise.demo.controller;

import com.practise.demo.dto.UserDetailsDTO;
import com.practise.demo.service.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UsersServiceImpl userService;

    @Autowired
    private Environment environment;

    @GetMapping("/health")
    public String status(){
        return "Working on Port "+environment.getProperty("local.server.port");
    }

    @GetMapping
    public Collection<UserDetailsDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable String userId) {
        UserDetailsDTO userById = userService.getUserById(userId);
        if (null == userById) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userById, HttpStatus.OK);
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsDTO createNewUser(@RequestBody UserDetailsDTO userDetails) {
        return userService.createUser(userDetails);
    }

    @PutMapping(path ="/{userId}"
            , consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDetailsDTO> updateUser(@PathVariable String userId, @RequestBody UserDetailsDTO userDetails) {
        UserDetailsDTO updateUser = userService.updateUsers(userDetails, userId);
        if(Objects.nonNull(updateUser)){
            return new ResponseEntity<>(updateUser,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        boolean isRemoved = userService.deleteUserById(userId);
        if(isRemoved){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
