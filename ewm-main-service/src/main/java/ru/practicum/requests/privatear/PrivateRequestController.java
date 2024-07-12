package ru.practicum.requests.privatear;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.privatear.service.PrivateRequestService;
import ru.practicum.utils.PathConstants;

import java.util.List;

@RestController
@RequestMapping(PathConstants.USERS + PathConstants.USER_ID + PathConstants.REQUESTS)
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    @GetMapping
    public List<ParticipationRequestDto> getAll(@PathVariable long userId) {
        log.info("Получение спика всех заявок пользователя с userId {}", userId);
        return privateRequestService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable long userId,
                                        @RequestParam long eventId) {
        log.info("Сохранение новой заявки на событие с eventId {}, пользователем с userId {}", eventId, userId);
        return privateRequestService.save(userId, eventId);
    }

    @PatchMapping(PathConstants.BY_ID + PathConstants.CANCEL)
    public ParticipationRequestDto cancel(@PathVariable long userId,
                                          @PathVariable long id) {
        log.info("Отмена заявки с id {} пользователем с userId {}", id, userId);
        return privateRequestService.cancel(userId, id);
    }
}