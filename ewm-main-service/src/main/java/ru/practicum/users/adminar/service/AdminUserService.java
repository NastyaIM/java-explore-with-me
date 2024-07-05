package ru.practicum.users.adminar.service;

import org.springframework.data.domain.Page;
import ru.practicum.users.dto.UserDto;
import ru.practicum.utils.PageDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> get(List<Long> ids, PageDto pageParams);
    UserDto save(UserDto userDto);
    void delete(long id);
}
