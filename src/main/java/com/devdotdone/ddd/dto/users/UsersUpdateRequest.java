package com.devdotdone.ddd.dto.users;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UsersUpdateRequest {
    private int userId;
    private String userEmail;
    private String userPassword;
    private String userIntro;
    private MultipartFile ufAttach;
    private List<Integer> tagIds;
}
