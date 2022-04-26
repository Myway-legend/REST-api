package com.deanery.controller;

import com.deanery.entity.User;
import com.deanery.exception.InvalidPasswordException;
import com.deanery.repository.UserRepository;
import com.deanery.security.jwt.JwtProperties;
import com.deanery.security.jwt.JwtTokenProvider;
import com.deanery.web.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/deanery-api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
    PasswordEncoder pwdEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.pwdEncoder = passwordEncoder;
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest request) {
        try {
            String name = request.getUserName();

            User user = userRepository.findUserByUserName(name)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!pwdEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Invalid password");
            }

            String token = jwtTokenProvider.createToken(
                    name,
                    user.getRoles()
            );

            Map<String, String> model = new HashMap<>();
            model.put("userName", name);
            model.put("token", token);

            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
