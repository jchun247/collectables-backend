package io.github.jchun247.collectables.service.user;

import io.github.jchun247.collectables.dto.user.UserEntityDTO;
import io.github.jchun247.collectables.exception.ResourceNotFoundException;
import io.github.jchun247.collectables.mapper.UserMapper;
import io.github.jchun247.collectables.model.user.UserEntity;
import io.github.jchun247.collectables.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserEntityDTO getUserByAuth0Id(String auth0Id) {
        return userMapper.toUserEntityDto(userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found")));
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
        return userRepository.findByAuth0Id(auth0Id)
                .map(existingUser -> {
                    existingUser.setLastLogin(LocalDateTime.now());
                    existingUser.setEmail(email);
                    existingUser.setUsername(username);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> createUser(auth0Id, email, username));
    }
}
