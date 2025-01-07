package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.UserEntity;
import io.github.jchun247.collectables.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/user")
    public UserEntity getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String auth0Id = jwt.getSubject();
        String email = jwt.getClaim("email");
        String username = jwt.getClaim("username");

        return userService.provisionUser(auth0Id, email, username);
    }

}
