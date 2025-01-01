package com.practise.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class UserEntity implements Serializable{
    private String userId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String userType;
}
