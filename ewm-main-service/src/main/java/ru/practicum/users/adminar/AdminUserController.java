package ru.practicum.users.adminar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.adminar.service.AdminUserService;
import ru.practicum.users.dto.NewUserDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.ADMIN + PathConstants.USERS)
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserDto> get(@RequestParam(defaultValue = "") List<Long> ids,
                             @Valid PageParams pageParams) {
        log.info("Получение списка пользователей с id {}", ids);
        return adminUserService.get(ids, pageParams);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody NewUserDto userDto) {
        log.info("Сохранение пользователя {}", userDto);
        return adminUserService.save(userDto);
    }

    @DeleteMapping(PathConstants.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Удаление пользователя с id {}", id);
        adminUserService.delete(id);
    }
}