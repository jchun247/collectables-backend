package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetCurrentUser() {
        Jwt jwt  = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "auth0|123456")
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setAuth0Id("auth0|123456");

        when(userService.provisionUser("auth0|123456")).thenReturn(userEntity);

        UserEntity result = userController.getCurrentUser(jwt);

        assertEquals("auth0|123456", result.getAuth0Id());
    }

    @Test
    void testUpdateUserLogin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAuth0Id("auth0|123456");
        userEntity.setLastLogin(LocalDateTime.now().minusDays(1));

        when(userService.updateUserLastLogin("auth0|123456")).thenAnswer(invocation -> {
            userEntity.setLastLogin(LocalDateTime.now());
            return userEntity;
        });

        ResponseEntity<UserEntity> response = userController.updateUserLogin("auth0|123456");

        assertEquals("auth0|123456", response.getBody().getAuth0Id());
        assertTrue(response.getBody().getLastLogin().isAfter(userEntity.getLastLogin().minusSeconds(1)));
    }
}
