package com.kg.platform.online_course.dto.response;

import com.kg.platform.online_course.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddAdminResponse {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private String name;
    private String surname;
    private String phoneNumber;
}
