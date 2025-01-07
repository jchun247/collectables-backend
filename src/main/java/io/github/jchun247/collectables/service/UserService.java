package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.model.UserEntity;

public interface UserService {
    UserEntity getUserByAuth0Id(String auth0Id);
    UserEntity createUser(String auth0Id, String email, String username);
    UserEntity provisionUser(String auth0Id, String email, String username);
}
