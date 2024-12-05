package io.github.jchun247.collectables.repository;

import io.github.jchun247.collectables.model.ERole;
import io.github.jchun247.collectables.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
