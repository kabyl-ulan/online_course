package com.kg.platform.online_course.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kg.platform.online_course.dto.request.AddAdminRequest;
import com.kg.platform.online_course.dto.request.LoginRequest;
import com.kg.platform.online_course.dto.request.RegisterRequest;
import com.kg.platform.online_course.dto.response.AddAdminResponse;
import com.kg.platform.online_course.dto.response.AuthResponse;
import com.kg.platform.online_course.dto.response.UserResponse;
import com.kg.platform.online_course.exceptions.BadRequestException;
import com.kg.platform.online_course.exceptions.ECommerceException;
import com.kg.platform.online_course.exceptions.NotFoundException;
import com.kg.platform.online_course.mappers.UserMapper;
import com.kg.platform.online_course.models.User;
import com.kg.platform.online_course.models.enums.Role;
import com.kg.platform.online_course.repositories.UserRepository;
import com.kg.platform.online_course.security.jwt.JwtUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager manager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource("serviceAccountKey.json")
                        .getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials).build();
        FirebaseApp.initializeApp(firebaseOptions);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new BadCredentialsException("Password or email not found"));
        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(user.getEmail(), token, user.getRole());
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new BadRequestException("This email: " + request.getEmail() + " is always use!");
        }
        if (request.getPassword1().equals(request.getPassword2())) {

            User user = new User(request);
            user.setPassword(passwordEncoder.encode(request.getPassword1()));
            user.setRole(Role.USER);
            userRepository.save(user);
            String token = jwtUtils.generateToken(user.getEmail());

            return new AuthResponse(user.getEmail(), token, user.getRole());
        } else throw new BadRequestException("The passwords not matches");
        //I need to look again
    }

    public UserResponse findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponse).orElseThrow(NotFoundException::new);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ECommerceException("User not found"));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUserId(Long id, User user) {
        user.setId(id);
        userRepository.save(user);
        return userRepository.save(user);
    }

    public AddAdminResponse addAdmin(AddAdminRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new BadRequestException("This email: " + request.getEmail() + " is always use!");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .surname(request.getSurname())
                .name(request.getName())
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
        return new AddAdminResponse(user.getId(), user.getEmail(), request.getPassword(), user.getRole(),
                user.getName(), user.getSurname(), user.getPhoneNumber());
    }

    public List<UserResponse> getUsers() {
        return userRepository.findUsers().stream().map(userMapper::toUserResponse).toList();
    }
    public AuthResponse authWithGoogleAccount(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
        if (!userRepository.existsUserByEmail(firebaseToken.getEmail())) {
            String[] name = firebaseToken.getName().split(" ");
            User newUser = User.builder()
                    .email(firebaseToken.getEmail())
                    .password(firebaseToken.getEmail())
                    .name(name[0])
                    .surname(name[1])
                    .phoneNumber("null")
                    .role(Role.USER)
                    .build();
            userRepository.save(newUser);
        }
        User user = userRepository.findUserByEmail(firebaseToken.getEmail());
        return new AuthResponse(user.getEmail(),
                jwtUtils.generateToken(user.getEmail()),
                user.getRole());
    }
}
