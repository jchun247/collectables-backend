package io.github.jchun247.collectables.service.user;

import com.auth0.exception.Auth0Exception;

public interface Auth0ManagementService {
    void updateUserEmail(String auth0Id, String email) throws Auth0Exception;
    void updateUserNickname(String auth0Id, String newNickname) throws Auth0Exception;
}
