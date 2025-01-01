package com.practise.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDetailsDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String userType;

}
