package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.model.UserEntity;
import io.github.jchun247.collectables.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserEntity getUserByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity createUser(String auth0Id, String email, String username) {
        UserEntity newUser = new UserEntity();
        newUser.setAuth0Id(auth0Id);
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setLastLogin(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public UserEntity provisionUser(String auth0Id, String email, String username) {
        // Check if user exists in database
        Optional<UserEntity> existingUser = userRepository.findByAuth0Id(auth0Id);
        return existingUser.orElseGet(() -> createUser(auth0Id, email, username));
    }
}
