package io.github.jchun247.collectables.service.user;

import io.github.jchun247.collectables.model.user.UserEntity;

public interface UserService {
    UserEntity getUserByAuth0Id(String auth0Id);
    UserEntity createUser(String auth0Id);
    UserEntity updateUserLastLogin(String auth0Id);
    UserEntity provisionUser(String auth0Id);
}
