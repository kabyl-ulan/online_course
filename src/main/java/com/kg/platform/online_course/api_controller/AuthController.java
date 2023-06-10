package com.kg.platform.online_course.api_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.google.firebase.auth.FirebaseAuthException;
import com.kg.platform.online_course.dto.request.AddAdminRequest;
import com.kg.platform.online_course.dto.request.LoginRequest;
import com.kg.platform.online_course.dto.request.RegisterRequest;
import com.kg.platform.online_course.dto.response.AddAdminResponse;
import com.kg.platform.online_course.dto.response.AuthResponse;
import com.kg.platform.online_course.dto.response.UserResponse;
import com.kg.platform.online_course.models.User;
import com.kg.platform.online_course.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication API", description = "The authentication API (for authentication)")
public class AuthController {
    private UserService userService;

    @Operation(summary = "Retrieve Authentication Token",
            description = "This endpoint returns a JWT for authenticating further requests to the API")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @Operation(summary = "Registration", description = "The endpoint for register user")
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @Operation(summary = "Add admin", description = "The endpoint returns the added admin")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/add-admin")
    public AddAdminResponse addAdmin(@RequestBody AddAdminRequest request) {
        return userService.addAdmin(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserId(@PathVariable Long id, @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUserId(id, user), HttpStatus.OK);
    }

    @Operation(summary = "Authentication with google",description = "Authentication via Google using Firebase")
    @PostMapping("/authenticate/google")
    public AuthResponse authWithGoogleAccount(String tokenId) throws FirebaseAuthException {
        return userService.authWithGoogleAccount(tokenId);
    }
}
