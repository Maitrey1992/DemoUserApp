package com.practise.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserEntity {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailId;
}
