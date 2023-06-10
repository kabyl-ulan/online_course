package com.kg.platform.online_course.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role  implements GrantedAuthority {
    USER, ADMIN,SUPER_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
