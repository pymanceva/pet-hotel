package ru.modgy.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.user.dto.NewUserDto;
import ru.modgy.user.dto.UpdateUserDto;
import ru.modgy.user.dto.UserDto;
import ru.modgy.user.dto.mapper.UserMapper;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(Long requesterId, Boolean isActive) {
        User requester = findUserById(requesterId);

        checkAccessForBrowse(requester, Roles.ROLE_ADMIN);

        List<Roles> roles =
                Arrays.asList(Roles.values()).subList(requester.getRole().ordinal(), Roles.values().length);

        List<User> allUsers;
        if (Objects.isNull(isActive)) {
            allUsers = userRepository.findAllByRoleIn(roles).orElse(Collections.emptyList());
        } else {
            allUsers = userRepository.findAllByRoleInAndIsActive(roles, isActive).orElse(Collections.emptyList());
        }
        log.info("UserService: getAllUsers, requesterId={}, isActive={}, usersList size={}",
                requesterId, isActive, allUsers.size());
        return userMapper.map(allUsers);
    }

    @Transactional()
    @Override
    public UserDto addUser(Long requesterId, NewUserDto newUserDto) {
        User requester = findUserById(requesterId);
        User newUser = userMapper.toUser(newUserDto);

        checkAccessForEdit(requester, newUser);

        User addedUser = userRepository.save(newUser);
        log.info("userService: addUser, requesterId={}, newUserDto={},  newUser={}",
                requesterId, newUserDto, addedUser);
        return userMapper.toUserDto(addedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long requesterId, Long userId) {
        User requester = findUserById(requesterId);
        if (userId.equals(requesterId)) {
            log.info("UserService: getUserById, requesterId ={}, by userId={}", requesterId, userId);
            return userMapper.toUserDto(requester);
        }
        User user = findUserById(userId);
        checkAccessForBrowse(requester, user);

        log.info("UserService: getUserById, requesterId ={}, by userId={}", requesterId, userId);
        return userMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long requesterId, Long userId) {
        User requester = findUserById(requesterId);
        User user = findUserById(userId);

        if (user.getRole().equals(Roles.ROLE_BOSS)) {
            throw new AccessDeniedException("User with role=ROLE_BOSS can't delete");
        }

        checkAccessForEdit(requester, user);

        Integer result = userRepository.deleteUserById(userId);
        if (result == 0) {
            throw new NotFoundException(String.format("user with id=%d not found", userId));
        }
        log.info("UserService: deleteUserById, requesterId={} userId={}", requesterId, user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long requesterId, Long userId, UpdateUserDto updateUserDto) {
        boolean updateBySelf = requesterId.equals(userId);
        User requester = findUserById(requesterId);

        User newUser = userMapper.toUser(updateUserDto);
        newUser.setId(userId);
        User oldUser;

        if (updateBySelf) {
            oldUser = requester;
        } else {
            checkAccessForEdit(requester, newUser);
            oldUser = findUserById(userId);
            checkAccessForEdit(requester, oldUser);
        }

        if (Objects.isNull(newUser.getLastName())) {
            newUser.setLastName(oldUser.getLastName());
        }
        if (Objects.isNull(newUser.getFirstName())) {
            newUser.setFirstName(oldUser.getFirstName());
        }
        if (Objects.isNull(newUser.getMiddleName())) {
            newUser.setMiddleName(oldUser.getMiddleName());
        }
        if (Objects.isNull(newUser.getPassword())) {
            newUser.setPassword(oldUser.getPassword());
        }
        if (Objects.isNull(newUser.getEmail())) {
            newUser.setEmail(oldUser.getEmail());
        }
        if (Objects.isNull(newUser.getRole()) || updateBySelf) {
            newUser.setRole(oldUser.getRole());
        }
        if (Objects.isNull(newUser.getIsActive()) || updateBySelf) {
            newUser.setIsActive(oldUser.getIsActive());
        }
        User updatedUser = userRepository.save(newUser);
        log.info("UserService: updateUser, requesterId={}, userId={}, to updateUserDto={}",
                requesterId, userId, updateUserDto);

        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto setUserState(Long requesterId, Long userId, Boolean isActive) {
        User requester = findUserById(requesterId);
        User user = findUserById(userId);
        checkAccessForEdit(requester, user);

        user.setIsActive(isActive);

        User updatedUser = userRepository.save(user);
        return userMapper.toUserDto(updatedUser);
    }

    private void checkAccessForEdit(User requester, Roles role) {
        if (requester.getRole().ordinal() < 2 &&
                (role == null ||
                        (requester.getRole().ordinal() < role.ordinal()))
        ) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for edit this information",
                requester.getRole()));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("user with id=%d not found", userId)));
    }

    private void checkAccessForEdit(User requester, User user) {
        checkAccessForEdit(requester, user.getRole());
    }

    private void checkAccessForBrowse(User requester, Roles role) {
        if (requester.getRole().ordinal() < 2 &&
                requester.getRole().ordinal() <= role.ordinal()) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for browsing this information",
                requester.getRole()));
    }

    private void checkAccessForBrowse(User requester, User user) {
        checkAccessForBrowse(requester, user.getRole());
    }
}
