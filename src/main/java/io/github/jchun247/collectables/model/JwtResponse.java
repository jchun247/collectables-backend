package io.github.jchun247.collectables.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}
