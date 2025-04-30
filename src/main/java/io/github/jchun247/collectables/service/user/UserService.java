package io.github.jchun247.collectables.service.user;

import io.github.jchun247.collectables.dto.user.UserEntityDTO;
import io.github.jchun247.collectables.model.user.UserEntity;

public interface UserService {
    UserEntityDTO getUserByAuth0Id(String auth0Id);
    UserEntity createUser(String auth0Id, String email, String username);
    UserEntity provisionUser(String auth0Id, String email, String username);
}
