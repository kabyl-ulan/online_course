package com.kg.platform.online_course.models;

import com.kg.platform.online_course.dto.request.RegisterRequest;
import com.kg.platform.online_course.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    private static final String SEQ_NAME = "user_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1, initialValue = 3)
    private Long id;
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private String password;
    @NotBlank
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(RegisterRequest request){
        this.name = request.getName();
        this.surname = request.getSurname();
        this.email = request.getEmail();
        this.password = request.getPassword1();
        this.phoneNumber = request.getPhoneNumber();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
