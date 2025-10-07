package com.devdotdone.ddd.dto.users;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UsersSignupRequest {
    private Users user;
    @NotEmpty(message = "태그는 최소 1개 이상 선택해야 합니다.")
    private List<Integer> tagIds;
}
