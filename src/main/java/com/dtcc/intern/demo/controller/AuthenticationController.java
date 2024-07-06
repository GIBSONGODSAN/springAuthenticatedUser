package com.dtcc.intern.demo.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtcc.intern.demo.reqres.*;
import com.dtcc.intern.demo.handler.ResponseHandler;
import com.dtcc.intern.demo.model.*;
import com.dtcc.intern.demo.service.*;


@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserAccount> register(@RequestBody RegisterUserDto registerUserDto) {
        UserAccount registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginUserDto loginUserDto) {
        UserAccount authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        Map<String, Object> loginResponse = new HashMap<>();
        loginResponse.put("user", authenticatedUser);
        loginResponse.put("token", jwtToken);
        return ResponseHandler.generateResponse("User authenticated",HttpStatus.OK,loginResponse);
    }
}
