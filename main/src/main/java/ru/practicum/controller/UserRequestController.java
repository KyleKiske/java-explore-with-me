package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.UserRequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class UserRequestController {

    private final UserRequestService userRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable long userId) {
        List<ParticipationRequestDto> requestList = userRequestService.getUserRequests(userId);
        log.info("Получен список заявок на участие пользователя {}", userId);
        return requestList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addUserRequest(@PathVariable Long userId,
                                                  @RequestParam Long eventId) {
        ParticipationRequestDto request = userRequestService.addUserRequest(userId, eventId);
        log.info("Создана новая заявка");
        return request;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequestEventById(
                                            @PathVariable Long userId,
                                            @PathVariable Long requestId) {
        ParticipationRequestDto cancelUserRequest = userRequestService.cancelUserRequest(userId, requestId);
        log.info("Заявка {} отозвана", requestId);
        return cancelUserRequest;
    }
}
