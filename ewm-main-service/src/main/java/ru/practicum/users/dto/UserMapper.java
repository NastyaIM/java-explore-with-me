package ru.practicum.users.dto;

import org.mapstruct.Mapper;
import ru.practicum.users.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(NewUserDto userDto);
}
