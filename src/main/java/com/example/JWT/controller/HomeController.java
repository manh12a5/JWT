package com.example.JWT.controller;

import com.example.JWT.model.JWTRequest;
import com.example.JWT.model.JWTResponse;
import com.example.JWT.service.UserService;
import com.example.JWT.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (Exception e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtUtility.generateToken(userDetails);

        return new JWTResponse(token);
    }

}
