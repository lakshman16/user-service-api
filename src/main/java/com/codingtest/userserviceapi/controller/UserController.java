package com.codingtest.userserviceapi.controller;

import com.codingtest.userserviceapi.model.User;
import com.codingtest.userserviceapi.service.UserService;
import com.codingtest.userserviceapi.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    public static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;    // Single Source Of Truth For ALL User Related Operations

    @PostMapping("/user/create")
    public ResponseEntity<Void> saveUser(@RequestBody User user) {
        log.info("Creating User: {}", user);
        if(Optional.ofNullable(user).isPresent()) {
            if(userService.isUserExists(user.getSsn())) {
                log.error("Unable to create. A User with ssn {} already exist", user.getSsn());
                return new ResponseEntity(new CustomErrorType("Unable to create. A User with ssn " +
                        user.getSsn() + " already exist."),HttpStatus.CONFLICT);

            }
        }
        userService.saveUser(user);
        URI location = ServletUriComponentsBuilder.fromPath("/api/users").path("/{ssn}").buildAndExpand(user.getSsn()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers() {
        log.info("Fetching Users");
        List<User> users = userService.findAllUsers();
        if(users.isEmpty()) {
            log.error("No Users");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{ssn}")
    public ResponseEntity<User> findUserBySsn(@PathVariable long ssn) {
        log.info("Fetching User with ssn {}", ssn);
        User user = userService.findUserBySsn(ssn);

        if(user == null) {
            log.error("User with ssn {} not found", ssn);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{ssn}")
    public ResponseEntity<Void> deleteUserBySsn(@PathVariable long ssn) {
        log.info("Deleting User with ssn {}", ssn);
        User user = userService.findUserBySsn(ssn);

        if(user == null) {
            log.error("Unable to delete. User with ssn {} not found", ssn);
            return ResponseEntity.notFound().build();
        }
        userService.deleteUserBySsn(ssn);
        return ResponseEntity.noContent().build();
    }


}
