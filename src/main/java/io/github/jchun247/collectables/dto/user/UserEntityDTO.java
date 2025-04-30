package io.github.jchun247.collectables.dto.user;

import lombok.Data;

@Data
public class UserEntityDTO {
    private Long id;
    private String auth0Id;
    private String email;
    private String username;
}
