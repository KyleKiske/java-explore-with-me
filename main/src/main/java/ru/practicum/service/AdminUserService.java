package ru.practicum.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PaginationMaker;
import ru.practicum.dto.notDto.NewUserRequest;
import ru.practicum.exception.DuplicateEmailException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<User> getFilteredUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllById(ids);
        } else {
            return userRepository.findAll(PaginationMaker.makePageRequest(from, size)).getContent();
        }
    }

    @Transactional
    public User createUser(NewUserRequest newUserRequest) {
        User user = userMapper.newUserToUser(newUserRequest);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        userRepository.deleteById(userId);
    }
}
