package io.github.jchun247.collectables.service.user;

import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.model.UserEntity;
import io.github.jchun247.collectables.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserEntity getUserByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity createUser(String auth0Id) {
        UserEntity newUser = new UserEntity();
        newUser.setAuth0Id(auth0Id);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public UserEntity updateUserLastLogin(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(user -> {
                    user.setLastLogin(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with auth0Id: " + auth0Id));
    }

    public UserEntity provisionUser(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(existingUser -> {
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> createUser(auth0Id));
    }
}
