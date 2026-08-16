package ru.nersus.storage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nersus.storage.dto.AuthRqDto;
import ru.nersus.storage.entity.User;

@Mapper(componentModel = "spring")
public interface AuthRqDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authenticated", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(source = "username", target = "email")
    @Mapping(source = "password", target = "passwordHash")
    User toEntity(AuthRqDto authRqDto);
}
