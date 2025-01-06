package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.UserEntity;
import io.github.jchun247.collectables.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/login")
    public UserEntity handleLogin(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("username");

        return userService.createOrUpdateUser(auth0Id, email, username);
    }

//    @GetMapping("/me")
//    public UserEntity getCurrentUser() {
//        return user
//    }
}
