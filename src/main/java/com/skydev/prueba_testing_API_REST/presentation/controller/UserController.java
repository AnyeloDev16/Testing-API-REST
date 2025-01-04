package com.skydev.prueba_testing_API_REST.presentation.controller;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;
import com.skydev.prueba_testing_API_REST.service.interfaces.IUserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserEntityService userService;

    @GetMapping("/email/{email}")
    public ResponseEntity<UserEntity> findByEmail(@PathVariable String email) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByEmail(email));

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserEntity>> findAllByName(@PathVariable String name) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUserByName(name));

    }

    @GetMapping("/age/{minAge}/{maxAge}")
    public ResponseEntity<List<UserEntity>> findAllByAgeBetween(@PathVariable Integer minAge, @PathVariable Integer maxAge) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsersByAgeBetween(minAge, maxAge));

    }

}
