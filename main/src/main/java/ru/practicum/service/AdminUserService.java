package ru.practicum.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.notDto.NewUserRequest;
import ru.practicum.exception.DuplicateEmailException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserDto> getFilteredUsers(List<Long> ids, Integer from, Integer size) {
        List<User> userList;
        if (ids != null && !ids.isEmpty()) {
            userList = userRepository.findAllById(ids);
        } else {
            userList = userRepository.findAll(PaginationMaker.makePageRequest(from, size)).getContent();
        }
        return userList.stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = userMapper.newUserToUser(newUserRequest);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    public void deleteUserById(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        userRepository.deleteById(userId);
    }
}
