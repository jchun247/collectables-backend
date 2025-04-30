package io.github.jchun247.collectables.dto.security;

import lombok.Data;

@Data
public class ProvisionUserRequestDTO {
    private String auth0Id;
    private String email;
    private String username;
}
