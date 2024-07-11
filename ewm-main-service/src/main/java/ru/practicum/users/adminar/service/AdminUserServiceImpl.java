package ru.practicum.users.adminar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.DataIntegrityViolationException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.NewUserDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.PageParams;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> get(List<Long> ids, PageParams pageParams) {
        List<User> users;
        PageRequest page = pageParams.getPageRequest();
        if (ids.isEmpty()) {
            users = userRepository.findAll(page).getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, page).getContent();
        }
        return users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto save(NewUserDto userDto) {
        try {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        } catch (RuntimeException e) {
            throw new DataIntegrityViolationException("Email is already exists");
        }

    }

    @Override
    public void delete(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", id)));
        userRepository.deleteById(id);
    }
}
