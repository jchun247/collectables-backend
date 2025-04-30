package io.github.jchun247.collectables.mapper;

import io.github.jchun247.collectables.dto.user.UserEntityDTO;
import io.github.jchun247.collectables.model.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntityDTO toUserEntityDto(UserEntity userEntity);
}
