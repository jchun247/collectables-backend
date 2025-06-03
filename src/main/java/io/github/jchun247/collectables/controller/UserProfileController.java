package io.github.jchun247.collectables.controller;

import com.auth0.exception.APIException;
import io.github.jchun247.collectables.dto.user.UpdateProfileRequest;
import io.github.jchun247.collectables.service.user.Auth0ManagementService;
import io.github.jchun247.collectables.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {
    private final Auth0ManagementService auth0ManagementService;
    private final UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal Jwt jwt, @RequestBody UpdateProfileRequest updateProfileRequest) {
        if (jwt == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String auth0UserId = jwt.getSubject(); // Get user ID from their access token

        try {
            // --- Update Auth0 ---
            if (updateProfileRequest.getEmail() != null && !updateProfileRequest.getEmail().isEmpty()) {
                auth0ManagementService.updateUserEmail(auth0UserId, updateProfileRequest.getEmail());
            }
            if (updateProfileRequest.getNickname() != null && !updateProfileRequest.getNickname().isEmpty()) {
                auth0ManagementService.updateUserNickname(auth0UserId, updateProfileRequest.getNickname());
            }

            // --- Optional: Update Local DB ---
            // If you maintain a local copy of user data, update it here AFTER
            // successfully updating Auth0. You might fetch the updated data
            // from Auth0 again or just use the data from the request.
            // Example: userService.updateLocalUser(auth0UserId, updates.getEmail(), updates.getNickname());

            return ResponseEntity.ok("Profile updated successfully");

        } catch (APIException e) {
            // Handle specific Auth0 errors (e.g., invalid input, permission denied)
            log.error("Error updating profile in Auth0: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body("Failed to update profile in Auth0: " + e.getMessage());
        } catch (Exception e) {
            // Handle other potential errors (e.g., local DB update failure)
            log.error("An error occurred while updating the profile: {}", e.getMessage());
            return ResponseEntity.status(500).body("An internal error occurred.");
        }
    }
}
