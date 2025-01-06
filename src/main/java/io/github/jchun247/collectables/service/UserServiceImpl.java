package io.github.jchun247.collectables.service;

import io.github.jchun247.collectables.model.UserEntity;
import io.github.jchun247.collectables.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserEntity createOrUpdateUser(String auth0Id, String email, String username) {
        return userRepository.findById(auth0Id)
                .map(existingUser -> {
                    existingUser.setEmail(email);
                    existingUser.setUsername(username);
                    existingUser.setLastLogin(LocalDateTime.now());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setId(auth0Id);
                    newUser.setEmail(email);
                    newUser.setUsername(username);
                    newUser.setCreatedAt(LocalDateTime.now());
                    newUser.setLastLogin(LocalDateTime.now());
                    return userRepository.save(newUser);
                });
    }
}
