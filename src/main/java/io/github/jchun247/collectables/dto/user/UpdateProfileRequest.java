package io.github.jchun247.collectables.dto.user;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;
    private String nickname;
}
