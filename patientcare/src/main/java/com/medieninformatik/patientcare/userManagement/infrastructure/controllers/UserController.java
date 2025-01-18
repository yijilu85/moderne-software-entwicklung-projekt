package com.medieninformatik.patientcare.userManagement.infrastructure.controllers;

import com.medieninformatik.patientcare.userManagement.domain.model.shared.User;
import com.medieninformatik.patientcare.userManagement.domain.model.valueObjects.LoginRequest;
import com.medieninformatik.patientcare.userManagement.infrastructure.Exceptions.InvalidCredentialsException;
import com.medieninformatik.patientcare.userManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping
    public List<User> findAllUsers() {
        return userService.getAllUsers();
    }

    @CrossOrigin
    @GetMapping(path = "/{id}")
    public Optional<User> findUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

//    @CrossOrigin
//    @GetMapping(path = "/login")
//    public ResponseEntity<User> loginUser(
//            @RequestParam("email") String email,
//            @RequestParam("password") String password) {
//        try {
//            User user = userService.loginUser(email, password);
//            return ResponseEntity.ok(user);
//        } catch (InvalidCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }

    @CrossOrigin
    @PostMapping(path = "/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(user);
        } catch (InvalidCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
