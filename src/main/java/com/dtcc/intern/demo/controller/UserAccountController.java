package com.dtcc.intern.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dtcc.intern.demo.repository.*;
import com.dtcc.intern.demo.model.*;
import com.dtcc.intern.demo.handler.ResponseHandler;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    @Autowired
    UserAccountRepository userAccountRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserAccount userAccount) {
        if (userAccountRepository.findByUsername(userAccount.getUsername()).isPresent()) {
            return ResponseHandler.generateResponse("Username is already taken", HttpStatus.BAD_REQUEST, null);
        }

        userAccount.setPassword(userAccount.getPassword());
        userAccount.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userAccountRepository.save(userAccount);
        return ResponseHandler.generateResponse("User account created", HttpStatus.CREATED, userAccount);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserAccount loginRequest) {
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(loginRequest.getUsername());
        if (userAccount.isPresent() && loginRequest.getPassword().equals(userAccount.get().getPassword())) {
            return ResponseHandler.generateResponse("User account found", HttpStatus.OK, userAccount.get());
        } else {
            return ResponseHandler.generateResponse("Invalid username or password", HttpStatus.UNAUTHORIZED, null);
        }
    }
}
