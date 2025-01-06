package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.model.UserEntity;

public interface UserService {
    public UserEntity createOrUpdateUser(String auth0Id, String email, String username);
}
