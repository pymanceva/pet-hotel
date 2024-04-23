package ru.modgy.user.service;

import ru.modgy.user.dto.NewUserDto;
import ru.modgy.user.dto.UpdateUserDto;
import ru.modgy.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(Long requesterId, NewUserDto newUserDto);

    UserDto getUserById(Long requesterId, Long userId);

    UserDto updateUser(Long requesterId, Long userId, UpdateUserDto userDto);

    Collection<UserDto> getAllUsers(Long requesterId, Boolean isActive);

    void deleteUserById(Long requesterId, Long userId);

    UserDto setUserState(Long requesterId, Long userId, Boolean isActive);
}
