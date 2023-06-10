package com.kg.platform.online_course.dto.response;

import com.kg.platform.online_course.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private String nickname;
    private String token;
    private Role role;
}
