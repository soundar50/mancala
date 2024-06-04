package com.bol.mancala.controller;

import com.bol.mancala.model.dto.UserDto;
import com.bol.mancala.service.UserService;
import com.bol.mancala.util.CopyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/mancala/v1/users")
@Tag(name = "User API", description = "API for user management")
public class UserController {

    @Autowired
    private final UserService userService;

    @Operation(summary = "Create a new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDto> createUser(@Parameter(description = "Display name of the user", required = true) @NotBlank @RequestParam final String displayName) {
        return userService.create(displayName)
                .map(CopyUtil::toUserDto);
    }

    @Operation(summary = "Get all users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .map(CopyUtil::toUserDto);
    }

}
