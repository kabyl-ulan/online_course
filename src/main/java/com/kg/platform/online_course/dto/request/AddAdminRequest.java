package com.kg.platform.online_course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddAdminRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
}
