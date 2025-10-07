package com.devdotdone.ddd.dto.users;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsersUpdateRequest {
    private int userId;
    private String userEmail;
    private String userPassword;
    @Size(max = 100, message = "한 마디는 최대 100자까지 입력 가능합니다.")
    private String userIntro;
    private MultipartFile ufAttach;
    private List<Integer> tagIds;
}
