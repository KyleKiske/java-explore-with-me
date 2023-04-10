package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.dto.notDto.NewUserRequest;
import ru.practicum.model.User;

@Component
public class UserMapper {

    public User newUserToUser(NewUserRequest newUserRequest) {
        if (newUserRequest == null) {
            return null;
        }
        User user = new User();

        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());

        return user;
    }

    public UserShortDto userToShortDto(User user) {
        if (user == null) {
            return null;
        }
        UserShortDto userShortDto = new UserShortDto();

        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());

        return userShortDto;
    }

    public UserDto userToUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());

        return userDto;
    }
}
