package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.payLoad.LoginRequest;
import com.example.demo.payLoad.LoginResponse;
import com.example.demo.payLoad.Message;
import com.example.demo.payLoad.dto.AuthDTO;
import com.example.demo.payLoad.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<Message> registryUser(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(new Message("Error: Username is already taken!"));
        } else {
            User user = new User();
            Account account = new Account();
            account.setUsername(userDTO.getUsername());
            account.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setAccount(account);
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setDateOfBirth(LocalDate.parse(userDTO.getDateOfBirth()));
            user.setGender(userDTO.getGender());
            userRepository.save(user);
            return ResponseEntity.ok(new Message("Success: User registered successfully!"));
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<AuthDTO> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        return ResponseEntity.ok(userService.validateToken(token));
    }
}
