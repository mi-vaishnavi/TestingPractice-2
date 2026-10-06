package com.vaishu.testing.Controllers;

import com.vaishu.testing.Model.Users;
import com.vaishu.testing.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/")
    public String greet(){
        return "Hello user";
    }
    @PostMapping("/users")
    public ResponseEntity<Users> register(@Valid @RequestBody Users user){
        Users savedUser =  service.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
