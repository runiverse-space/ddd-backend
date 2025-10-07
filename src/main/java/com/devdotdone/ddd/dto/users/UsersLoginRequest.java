package com.devdotdone.ddd.dto.users;

import lombok.Data;

@Data
public class UsersLoginRequest {
    private String userLoginId;
    private String userPassword;
}
