package io.github.jchun247.collectables.repository.user;

import io.github.jchun247.collectables.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByAuth0Id(String auth0Id);
}
