package ru.dogudacha.PetHotel.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.dto.mapper.UserMapper;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional()
    @Override
    public UserDto addUser(Long requesterId, UserDto newUserDto) {
        User requester = findUserById(requesterId);
        User newUser = userMapper.toUser(newUserDto);

        checkAccessForEdit(requester, newUser);

        User addedUser = userRepository.save(newUser);
        log.info("userService: was add user={}", addedUser);
        return userMapper.toUserDto(addedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long requesterId, Long userId) {
        User requester = findUserById(requesterId);
        if (userId.equals(requesterId)) {
            log.info("userService: was returned user={}, by id={}", requester, requesterId);
            return userMapper.toUserDto(requester);
        }
        User user = findUserById(userId);

        checkAccessForBrowse(requester, user);

        log.info("userService: was returned user={}, by id={}", user, userId);
        return userMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long requesterId, Long userId, UpdateUserDto userDto) {
        boolean updateBySelf = requesterId.equals(userId);
        User requester = findUserById(requesterId);

        User newUser = userMapper.toUser(userDto);
        newUser.setId(userId);
        User oldUser;

        if (updateBySelf) {
            oldUser = requester;
        } else {
            checkAccessForEdit(requester, newUser);
            oldUser = findUserById(userId);
            checkAccessForEdit(requester, oldUser);
        }

        if (Objects.isNull(newUser.getName())) {
            newUser.setName(oldUser.getName());
        }

        if (Objects.isNull(newUser.getEmail())) {
            newUser.setEmail(oldUser.getEmail());
        }

        if (Objects.isNull(newUser.getRole()) || updateBySelf) {
            newUser.setRole(oldUser.getRole());
        }

        User updatedUser = userRepository.save(newUser);
        log.info("userService: old user={} update to new user={}", oldUser, updatedUser);

        return userMapper.toUserDto(updatedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(Long requesterId) {
        User requester = findUserById(requesterId);

        checkAccessForBrowse(requester, requester);

        List<Roles> roles =
                Arrays.asList(Roles.values()).subList(requester.getRole().ordinal(), Roles.values().length);

        List<User> allUsers = userRepository.findAllByRoleIn(roles).orElse(Collections.emptyList());

        log.info("userService: returned all {} users", allUsers.size());
        return userMapper.map(allUsers);
    }

    @Transactional
    @Override
    public void deleteUserById(Long requesterId, Long userId) {
        User requester = findUserById(requesterId);
        User user = findUserById(userId);

        checkAccessForEdit(requester, user);

        userRepository.deleteById(userId);
        log.info("userService: delete user={}", user);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("user with id=%d not found", userId)));
    }

    private void checkAccessForEdit(User requester, User newUser) {

        if (requester.getRole().ordinal() < 2 &&
                (newUser.getRole() == null ||
                        (requester.getRole().ordinal() < newUser.getRole().ordinal()))
        ) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for edit this information",
                requester.getRole()));
    }

    private void checkAccessForBrowse(User requester, User user) {
        if (requester.getRole().ordinal() < 2 &&
                requester.getRole().ordinal() <= user.getRole().ordinal()) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for browsing this information",
                requester.getRole()));
    }
}
