package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.notDto.NewUserRequest;
import ru.practicum.model.User;
import ru.practicum.service.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<User> getFilteredUsers(@RequestParam(required = false) List<Long> ids,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<User> result = adminUserService.getFilteredUsers(ids, from, size);
        log.info("Создан список пользователей по запросу администратора");
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        User user = adminUserService.createUser(newUserRequest);
        log.info("Создан пользователь {}", user.getId());
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        adminUserService.deleteUserById(userId);
        log.info("Пользователь {} удален", userId);
    }
}
