package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public UserEntity getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt.getSubject();
        return userService.provisionUser(auth0Id);
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserEntity> updateUserLogin(String auth0Id) {
        return ResponseEntity.ok(userService.updateUserLastLogin(auth0Id));
    }

}
