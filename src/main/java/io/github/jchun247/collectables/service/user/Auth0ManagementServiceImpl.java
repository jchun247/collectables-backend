package io.github.jchun247.collectables.service.user;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Auth0ManagementServiceImpl implements Auth0ManagementService {
    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.management.clientId}")
    private String clientId;

    @Value("${auth0.management.clientSecret}")
    private String clientSecret;

    @Value("${auth0.management.audience}")
    private String audience;

    private AuthAPI authAPI;

    @PostConstruct
    public void init() {
        // Initialize AuthAPI client used for getting Management API tokens
        this.authAPI = AuthAPI.newBuilder(domain, clientId, clientSecret).build();
    }

    /**
     * Retrieves a Management API token using Client Credentials flow.
     * Note: In a production app, cache this token until it's close to expiring.
     * @return A valid Management API access token.
     * @throws Auth0Exception if token retrieval fails.
     */
    private String getManagementApiToken() throws Auth0Exception {
        // Requesting a token for the Management API
        TokenHolder tokenHolder = authAPI.requestToken(audience).execute().getBody();
        return tokenHolder.getAccessToken();
    }

    /**
     * Updates the email address for a given Auth0 user.
     * Requires the M2M application to have 'update:users' permission.
     *
     * @param auth0UserId The ID of the user in Auth0 (e.g., "auth0|xxxxxxxx").
     * @param newEmail The new email address to set.
     * @throws Auth0Exception if the API call fails.
     */
    public void updateUserEmail(String auth0UserId, String newEmail) throws Auth0Exception {
        try {
            String apiToken = getManagementApiToken(); // Get a fresh token (or cached one)
            ManagementAPI mgmt = ManagementAPI.newBuilder(domain, apiToken).build();

            // Create a User object containing ONLY the fields to be updated
            // Do NOT fetch the full user and modify it, as this can lead to errors.
            User userUpdate = new User();
            userUpdate.setEmail(newEmail);
            mgmt.users().update(auth0UserId, userUpdate).execute();
            log.info("Successfully updated email for user: {}", auth0UserId); // Use proper logging
        } catch (APIException e) {
            // Log detailed API error
            log.error("Auth0 API Error updating email for user {}: Status={}, Code={}, Description={}",
                    auth0UserId,
                    e.getStatusCode(),
                    e.getError(),
                    e.getDescription(),
                    e
            );
            throw e;
        }
    }

    /**
     * Updates the username/nickname for a given Auth0 user.
     * Requires the M2M application to have 'update:users' permission.
     * Note: Updating 'username' might be restricted depending on connection type.
     * 'nickname' is generally more reliably updatable.
     *
     * @param auth0UserId The ID of the user in Auth0.
     * @param newNickname The new nickname to set.
     * @throws Auth0Exception if the API call fails.
     */
    public void updateUserNickname(String auth0UserId, String newNickname) throws Auth0Exception {
        try {
            String apiToken = getManagementApiToken();
            ManagementAPI mgmt = ManagementAPI.newBuilder(domain, apiToken).build();

            User userUpdate = new User();
            userUpdate.setNickname(newNickname);

            mgmt.users().update(auth0UserId, userUpdate).execute();
            log.info("Successfully updated nickname for user: {}", auth0UserId); // Use proper logging
        } catch (APIException e) {
            // Log detailed API error
            log.error("Auth0 API Error updating nickname for user {}: Status={}, Code={}, Description={}",
                    auth0UserId,
                    e.getStatusCode(),
                    e.getError(),
                    e.getDescription(),
                    e
            );
            throw e;
        }
    }
}
