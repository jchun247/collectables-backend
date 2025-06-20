package io.github.jchun247.collectables.controller;

import io.github.jchun247.collectables.dto.security.ProvisionUserRequestDTO;
import io.github.jchun247.collectables.dto.user.UserEntityDTO;
import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Value("${auth0.action.secret}")
    private String auth0ActionSecret;

    @GetMapping("/me")
    public ResponseEntity<UserEntityDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
         if (jwt == null) {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         String auth0Id = jwt.getSubject();
         try {
             UserEntityDTO userEntityDTO = userService.getUserByAuth0Id(auth0Id);
             return ResponseEntity.ok(userEntityDTO);
         } catch (Exception e) { // Catch specific exceptions like UserNotFound
             // User is authenticated via JWT but not found in your DB (shouldn't happen if provisioning works)
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         }
    }

    @PostMapping("/provision")
    public ResponseEntity<?> provisionUser(
            @RequestBody ProvisionUserRequestDTO request,
            @RequestHeader("X-Auth0-Action-Secret") String secretHeader) {

        if (!secureCompare(secretHeader, auth0ActionSecret)) {
            log.warn("Unauthorized attempt to provision user: Invalid secret");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret");
        }

        if (request.getAuth0Id() == null || request.getEmail() == null || request.getUsername() == null) {
            log.warn("Bad request to provision user: Missing details for Auth0 ID {}", request.getAuth0Id());
            return ResponseEntity.badRequest().body("Missing required user details");
        }

        log.info("Provisioning user via Auth0 Action: {}", request.getAuth0Id());
        try {
            // Call the existing service method which handles create or update
            UserEntity user = userService.provisionUser(
                    request.getAuth0Id(),
                    request.getEmail(),
                    request.getUsername()
            );
            // Return 204 No Content since user data isn't needed
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to provision user {} via Auth0 Action", request.getAuth0Id(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to provision user");
        }
    }

    private boolean secureCompare(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
}
