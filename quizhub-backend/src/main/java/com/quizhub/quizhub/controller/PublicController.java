package com.quizhub.quizhub.controller;

import com.quizhub.quizhub.model.User;
import com.quizhub.quizhub.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

@RestController
@RequestMapping(value="/public")
public class PublicController {

    @Autowired
    UserService userService;

    //Create User
    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser); // HTTP 201 CREATED
    }
}