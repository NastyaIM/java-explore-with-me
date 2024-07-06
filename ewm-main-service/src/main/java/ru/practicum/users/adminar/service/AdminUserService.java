package ru.practicum.users.adminar.service;

import ru.practicum.users.dto.UserDto;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface AdminUserService {
    List<UserDto> get(List<Long> ids, PageParams pageParams);

    UserDto save(UserDto userDto);

    void delete(long id);
}
