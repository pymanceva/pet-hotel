package ru.modgy.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.user.dto.NewUserDto;
import ru.modgy.user.dto.UpdateUserDto;
import ru.modgy.user.dto.UserDto;
import ru.modgy.user.dto.mapper.UserMapper;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    final long requesterId = 1L;
    final User requester = User.builder()
            .email("requester@mail.ru")
            .lastName("requester")
            .id(requesterId)
            .role(Roles.ROLE_BOSS)
            .build();
    long userId = 2L;
    final String userFirstName = "new User FirstName";
    final String userMiddleName = "new User MiddleName";
    final String userLastName = "new User LastName";
    final String email = "newUser@mail.com";
    final String userPassword = "user_pwd";
    boolean isActive = true;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void addUser_whenAddUserAdminByBoss_thenUserAdded() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUserInput = new User(null, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUser = new User(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        UserDto userDto = new UserDto(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(newUserDto)).thenReturn(newUserInput);
        when(userRepository.save(newUserInput)).thenReturn(newUser);
        when(userMapper.toUserDto(newUser)).thenReturn(userDto);

        UserDto addedUserDto = userService.addUser(requesterId, newUserDto);

        assertAll(
                () -> assertEquals(addedUserDto, userDto),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUserInput),
                () -> verify(userMapper).toUserDto(newUser)
        );
    }

    @Test
    void addUser_whenAddUserAdminByAdmin_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUserInput = new User(null, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(newUserDto)).thenReturn(newUserInput);

        assertThrows(AccessDeniedException.class,
                () -> userService.addUser(requesterId, newUserDto));
    }

    @Test
    void addUser_whenAddUserUserByAdmin_thenUserAdded() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUserInput = new User(null, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUser = new User(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        UserDto userDto = new UserDto(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(newUserDto)).thenReturn(newUserInput);
        when(userRepository.save(newUserInput)).thenReturn(newUser);
        when(userMapper.toUserDto(newUser)).thenReturn(userDto);

        UserDto addedUserDto = userService.addUser(requesterId, newUserDto);

        assertAll(
                () -> assertEquals(addedUserDto, userDto),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUserInput),
                () -> verify(userMapper).toUserDto(newUser)
        );
    }

    @Test
    void addUser_whenAddUserUserByUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles userRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUserInput = new User(null, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(newUserDto)).thenReturn(newUserInput);

        assertThrows(AccessDeniedException.class,
                () -> userService.addUser(requesterId, newUserDto));
    }

    @Test
    void addUser_whenAddUserUserByBoss_thenUserAdded() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUserInput = new User(null, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        User newUser = new User(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        UserDto userDto = new UserDto(userId, userLastName, userFirstName, userMiddleName, email, userPassword,
                userRole, true);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(newUserDto)).thenReturn(newUserInput);
        when(userRepository.save(newUserInput)).thenReturn(newUser);
        when(userMapper.toUserDto(newUser)).thenReturn(userDto);

        UserDto addedUserDto = userService.addUser(requesterId, newUserDto);

        assertAll(
                () -> assertEquals(addedUserDto, userDto),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUserInput),
                () -> verify(userMapper).toUserDto(newUser)
        );
    }

    @Test
    void getUserById_whenAdminGetByBoss_thenReturnedUser() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        User expectedUser = new User();
        expectedUser.setRole(userRole);
        UserDto expectedUserDto = new UserDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);

        UserDto returnedUserDto = userService.getUserById(requesterId, userId);

        assertAll(
                () -> assertEquals(expectedUserDto, returnedUserDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUserDto(expectedUser)
        );
    }

    @Test
    void getUserById_whenAdminGetByAdmin_thenReturnedUser() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        User expectedUser = new User();
        expectedUser.setRole(userRole);
        UserDto expectedUserDto = new UserDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);

        UserDto returnedUserDto = userService.getUserById(requesterId, userId);

        assertAll(
                () -> assertEquals(expectedUserDto, returnedUserDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUserDto(expectedUser)
        );
    }

    @Test
    void getUserById_whenUserGetByBoss_thenReturnedUser() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        User expectedUser = new User();
        expectedUser.setRole(userRole);
        UserDto expectedUserDto = new UserDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);

        UserDto returnedUserDto = userService.getUserById(requesterId, userId);

        assertAll(
                () -> assertEquals(expectedUserDto, returnedUserDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUserDto(expectedUser)
        );
    }

    @Test
    void getUserById_whenUserGetByAdmin_thenReturnedUser() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        User expectedUser = new User();
        expectedUser.setRole(userRole);
        UserDto expectedUserDto = new UserDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);

        UserDto returnedUserDto = userService.getUserById(requesterId, userId);

        assertAll(
                () -> assertEquals(expectedUserDto, returnedUserDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUserDto(expectedUser)
        );
    }

    @Test
    void getUserById_whenUserGetBySelf_thenReturnedUser() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        userId = requesterId;
        User expectedUser = requester;
        UserDto expectedUserDto = new UserDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userMapper.toUserDto(expectedUser)).thenReturn(expectedUserDto);

        UserDto returnedUserDto = userService.getUserById(requesterId, userId);

        assertAll(
                () -> assertEquals(expectedUserDto, returnedUserDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUserDto(expectedUser)
        );
    }

    @Test
    void getUserById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(requesterId, userId));
    }

    @Test
    void getUserById_whenUserNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(requesterId, userId));
    }

    @Test
    void getUserById_whenBossGetByAdmin_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);
        User expectedUser = User.builder().role(userRole).build();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        assertThrows(AccessDeniedException.class,
                () -> userService.getUserById(requesterId, userId));
    }

    @Test
    void getUserById_whenAdminGetByUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        User expectedUser = User.builder().role(userRole).build();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        assertThrows(AccessDeniedException.class,
                () -> userService.getUserById(requesterId, userId));
    }

    @Test
    void updateUser_whenRequesterBossAndUserFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanIdAndIsActive() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles oldUserRole = Roles.ROLE_USER;
        Roles newUserRole = Roles.ROLE_FINANCIAL;
        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .lastName("new" + userLastName)
                .firstName("new" + userFirstName)
                .middleName("new" + userMiddleName)
                .email("new" + email)
                .role(newUserRole)
                .password("new" + userPassword)
                .build();

        User oldUser = User.builder()
                .id(userId)
                .lastName(userLastName)
                .firstName(userFirstName)
                .middleName(userMiddleName)
                .email(email)
                .role(oldUserRole)
                .password(userPassword)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        User userAfter = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        UserDto userDtoAfter = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);

        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(userDtoAfter, returnedUserDto,
                        "entity field test failed"),
                () -> assertEquals(userId, userForSave.getId(),
                        "id field test1 failed"),
                () -> assertEquals(oldUser.getId(), userForSave.getId(),
                        "id field test2 failed"),
                () -> assertEquals(newUserDto.getLastName(), userForSave.getLastName(),
                        "lastName field test failed"),
                () -> assertEquals(newUserDto.getFirstName(), userForSave.getFirstName(),
                        "firstName field test failed"),
                () -> assertEquals(newUserDto.getMiddleName(), userForSave.getMiddleName(),
                        "middleName field test failed"),
                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(),
                        "email field test failed"),
                () -> assertEquals(newUserDto.getRole(), userForSave.getRole(),
                        "role field test failed"),


                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUser),
                () -> verify(userMapper).toUserDto(userAfter)
        );
    }

    @Test
    void updateUser_whenRequesterBossAndUserFoundAndOnlyEmailNotNull_thenUpdateEmailOnly() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles oldUserRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .email("new" + email)
                .build();

        User oldUser = User.builder()
                .id(userId)
                .lastName(userLastName)
                .firstName(userFirstName)
                .middleName(userMiddleName)
                .email(email)
                .role(oldUserRole)
                .password(userPassword)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        User userAfter = User.builder()
                .id(oldUser.getId())
                .lastName(oldUser.getLastName())
                .firstName(oldUser.getFirstName())
                .middleName(oldUser.getMiddleName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .password(oldUser.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        UserDto userDtoAfter = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);

        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(userDtoAfter, returnedUserDto,
                        "entity field test failed"),
                () -> assertEquals(oldUser.getId(), userForSave.getId(),
                        "id field test2 failed"),
                () -> assertEquals(oldUser.getLastName(), userForSave.getLastName(),
                        "lastName field test failed"),
                () -> assertEquals(oldUser.getFirstName(), userForSave.getFirstName(),
                        "firstName field test failed"),
                () -> assertEquals(oldUser.getMiddleName(), userForSave.getMiddleName(),
                        "middleName field test failed"),
                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(),
                        "email field test failed"),
                () -> assertEquals(oldUser.getRole(), userForSave.getRole(),
                        "role field test failed"),

                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUser),
                () -> verify(userMapper).toUserDto(userAfter)
        );
    }

    @Test
    void updateUser_whenRequesterAdminAndUserFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanIdAndIsActive() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles oldUserRole = Roles.ROLE_USER;
        Roles newUserRole = Roles.ROLE_FINANCIAL;
        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .lastName("new" + userLastName)
                .firstName("new" + userFirstName)
                .middleName("new" + userMiddleName)
                .email("new" + email)
                .role(newUserRole)
                .password("new" + userPassword)
                .build();

        User oldUser = User.builder()
                .id(userId)
                .lastName(userLastName)
                .firstName(userFirstName)
                .middleName(userMiddleName)
                .email(email)
                .role(oldUserRole)
                .password(userPassword)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        User userAfter = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        UserDto userDtoAfter = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);

        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(userDtoAfter, returnedUserDto,
                        "entity field test failed"),
                () -> assertEquals(userId, userForSave.getId(),
                        "id field test1 failed"),
                () -> assertEquals(oldUser.getId(), userForSave.getId(),
                        "id field test2 failed"),
                () -> assertEquals(newUserDto.getLastName(), userForSave.getLastName(),
                        "lastName field test failed"),
                () -> assertEquals(newUserDto.getFirstName(), userForSave.getFirstName(),
                        "firstName field test failed"),
                () -> assertEquals(newUserDto.getMiddleName(), userForSave.getMiddleName(),
                        "middleName field test failed"),
                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(),
                        "email field test failed"),
                () -> assertEquals(newUserDto.getRole(), userForSave.getRole(),
                        "role field test failed"),


                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUser),
                () -> verify(userMapper).toUserDto(userAfter)
        );
    }

    @Test
    void updateUser_whenRequesterAdminAndUserFoundAndOnlyEmailNotNull_thenUpdateEmailOnly() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles oldUserRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .email("new" + email)
                .build();

        User oldUser = User.builder()
                .id(userId)
                .lastName(userLastName)
                .firstName(userFirstName)
                .middleName(userMiddleName)
                .email(email)
                .role(oldUserRole)
                .password(userPassword)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        User userAfter = User.builder()
                .id(oldUser.getId())
                .lastName(oldUser.getLastName())
                .firstName(oldUser.getFirstName())
                .middleName(oldUser.getMiddleName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .password(oldUser.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        UserDto userDtoAfter = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);

        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);
        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(userDtoAfter, returnedUserDto,
                        "entity field test failed"),
                () -> assertEquals(oldUser.getId(), userForSave.getId(),
                        "id field test2 failed"),
                () -> assertEquals(oldUser.getLastName(), userForSave.getLastName(),
                        "lastName field test failed"),
                () -> assertEquals(oldUser.getFirstName(), userForSave.getFirstName(),
                        "firstName field test failed"),
                () -> assertEquals(oldUser.getMiddleName(), userForSave.getMiddleName(),
                        "middleName field test failed"),
                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(),
                        "email field test failed"),
                () -> assertEquals(oldUser.getRole(), userForSave.getRole(),
                        "role field test failed"),

                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUser),
                () -> verify(userMapper).toUserDto(userAfter)
        );
    }

    @Test
    void updateUser_whenRequesterIsUserAndFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanIdAndRoleAndIsActive() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles newUserRole = Roles.ROLE_ADMIN;
        userId = requesterId;
        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .lastName("new" + requester.getLastName())
                .firstName("new" + requester.getFirstName())
                .middleName("new" + requester.getMiddleName())
                .email("new" + requester.getEmail())
                .role(newUserRole)
                .password("new" + requester.getPassword())
                .build();

        User oldUser = requester;

        User newUser = User.builder()
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .password(newUserDto.getPassword())
                .build();

        User userAfter = User.builder()
                .id(oldUser.getId())
                .lastName(newUserDto.getLastName())
                .firstName(newUserDto.getFirstName())
                .middleName(newUserDto.getMiddleName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .password(newUserDto.getPassword())
                .isActive(oldUser.getIsActive())
                .build();

        UserDto userDtoAfter = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userDtoAfter);

        UserDto returnedUserDto = userService.updateUser(requesterId, userId, newUserDto);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(userDtoAfter, returnedUserDto,
                        "entity field test failed"),
                () -> assertEquals(userId, userForSave.getId(),
                        "id field test1 failed"),
                () -> assertEquals(oldUser.getId(), userForSave.getId(),
                        "id field test2 failed"),
                () -> assertEquals(newUserDto.getLastName(), userForSave.getLastName(),
                        "lastName field test failed"),
                () -> assertEquals(newUserDto.getFirstName(), userForSave.getFirstName(),
                        "firstName field test failed"),
                () -> assertEquals(newUserDto.getMiddleName(), userForSave.getMiddleName(),
                        "middleName field test failed"),
                () -> assertEquals(newUserDto.getEmail(), userForSave.getEmail(),
                        "email field test failed"),
                () -> assertEquals(requester.getRole(), userForSave.getRole(),
                        "role field test failed"),


                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userMapper).toUser(newUserDto),
                () -> verify(userRepository).save(newUser),
                () -> verify(userMapper).toUserDto(userAfter)
        );
    }

    @Test
    void updateUser_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.updateUser(requesterId, userId, new UpdateUserDto()));
    }

    @Test
    void updateUser_whenUserNotFound_thenNotFoundException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userMapper.toUser(any(UpdateUserDto.class))).thenReturn(new User());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.updateUser(requesterId, userId, new UpdateUserDto()));
    }

    @Test
    void updateUser_whenAdminUpdateBoss_thenNotAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles oldUserRole = Roles.ROLE_BOSS;

        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .firstName(requester.getFirstName() + " new")
                .email("new " + requester.getEmail())
                .build();

        User oldUser = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(oldUserRole)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .firstName(newUserDto.getFirstName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);

        assertThrows(AccessDeniedException.class,
                () -> userService.updateUser(requesterId, userId, newUserDto));
    }

    @Test
    void updateUser_whenAdminUpdateUserToAdmin_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles oldUserRole = Roles.ROLE_USER;
        Roles newUserRole = Roles.ROLE_ADMIN;

        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .firstName(requester.getFirstName() + " new")
                .email("new " + requester.getEmail())
                .role(newUserRole)
                .build();

        User oldUser = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(oldUserRole)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .firstName(newUserDto.getFirstName())
                .email(newUserDto.getEmail())
                .role(newUserDto.getRole())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);

        assertThrows(AccessDeniedException.class,
                () -> userService.updateUser(requesterId, userId, newUserDto));
    }

    @Test
    void updateUser_whenUserUpdateAdmin_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles oldUserRole = Roles.ROLE_ADMIN;

        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .firstName(requester.getFirstName() + " new")
                .email("new " + requester.getEmail())
                .build();

        User oldUser = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(oldUserRole)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .firstName(newUserDto.getFirstName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);

        assertThrows(AccessDeniedException.class,
                () -> userService.updateUser(requesterId, userId, newUserDto));
    }

    @Test
    void updateUser_whenUserUpdateUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles oldUserRole = Roles.ROLE_USER;

        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .firstName(requester.getFirstName() + " new")
                .email("new " + requester.getEmail())
                .build();

        User oldUser = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(oldUserRole)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .firstName(newUserDto.getFirstName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);

        assertThrows(AccessDeniedException.class,
                () -> userService.updateUser(requesterId, userId, newUserDto));
    }

    @Test
    void updateUser_whenUserUpdateFinancial_thenNotAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles oldUserRole = Roles.ROLE_FINANCIAL;
        Roles newUserRole = null;

        requester.setRole(requesterRole);

        UpdateUserDto newUserDto = UpdateUserDto.builder()
                .firstName(requester.getFirstName() + " new")
                .email("new " + requester.getEmail())
                .build();

        User oldUser = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(oldUserRole)
                .build();

        User newUser = User.builder()
                .id(oldUser.getId())
                .firstName(newUserDto.getFirstName())
                .email(newUserDto.getEmail())
                .role(oldUser.getRole())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userMapper.toUser(newUserDto)).thenReturn(newUser);

        assertThrows(AccessDeniedException.class,
                () -> userService.updateUser(requesterId, userId, newUserDto));
    }

    @Test
    void getAllUsers_whenBossGetAllUsers_returnAllUsers() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole1 = Roles.ROLE_USER;
        Roles userRole2 = Roles.ROLE_ADMIN;
        Roles userRole3 = Roles.ROLE_FINANCIAL;
        Boolean isActive = null;
        boolean isActive1 = true;
        boolean isActive2 = false;
        boolean isActive3 = true;
        List<Roles> roles = Arrays.stream(Roles.values()).toList();
        requester.setRole(requesterRole);

        User user1 = User.builder()
                .id(userId + 1).firstName("1" + userFirstName).email("1" + email).role(userRole1).isActive(isActive1)
                .build();
        User user2 = User.builder()
                .id(userId + 2).firstName("2" + userFirstName).email("2" + email).role(userRole2).isActive(isActive2)
                .build();
        User user3 = User.builder()
                .id(userId + 3).firstName("3" + userFirstName).email("3" + email).role(userRole3).isActive(isActive3)
                .build();

        List<User> userList = List.of(user1, user2, user3);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(user1.getRole())
                .isActive(user1.getIsActive())
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(user2.getRole())
                .isActive(user2.getIsActive())
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(user3.getRole())
                .isActive(user3.getIsActive())
                .build();

        List<UserDto> userDtoList = List.of(userDto1, userDto2, userDto3);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findAllByRoleIn(roles)).thenReturn(Optional.of(userList));
        when(userMapper.map(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId, isActive);

        assertAll(
                () -> assertEquals(userDtoList, returnedUsersDto),
                () -> verify(userRepository).findAllByRoleIn(roles),
                () -> verify(userMapper).map(userList)
        );
    }

    @Test
    void getAllUsers_whenBossGetAllActiveUsers_returnAllActiveUsers() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole1 = Roles.ROLE_USER;
        Roles userRole2 = Roles.ROLE_ADMIN;
        Roles userRole3 = Roles.ROLE_FINANCIAL;
        Boolean isActive = true;
        boolean isActive1 = true;
        boolean isActive2 = false;
        boolean isActive3 = true;
        List<Roles> roles = Arrays.stream(Roles.values()).toList();
        requester.setRole(requesterRole);

        User user1 = User.builder()
                .id(userId + 1).firstName("1" + userFirstName).email("1" + email).role(userRole1).isActive(isActive1)
                .build();
        User user2 = User.builder()
                .id(userId + 2).firstName("2" + userFirstName).email("2" + email).role(userRole2).isActive(isActive2)
                .build();
        User user3 = User.builder()
                .id(userId + 3).firstName("3" + userFirstName).email("3" + email).role(userRole3).isActive(isActive3)
                .build();

        List<User> userList = List.of(user1, user3);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(user1.getRole())
                .isActive(user1.getIsActive())
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(user2.getRole())
                .isActive(user2.getIsActive())
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(user3.getRole())
                .isActive(user3.getIsActive())
                .build();

        List<UserDto> userDtoList = List.of(userDto1, userDto3);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findAllByRoleInAndIsActive(roles, isActive)).thenReturn(Optional.of(userList));
        when(userMapper.map(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId, isActive);

        assertAll(
                () -> assertEquals(userDtoList, returnedUsersDto),
                () -> verify(userRepository).findAllByRoleInAndIsActive(roles, isActive),
                () -> verify(userMapper).map(userList)
        );
    }

    @Test
    void getAllUsers_whenBossGetAllNotActiveUsers_returnAllNotActiveUsers() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole1 = Roles.ROLE_USER;
        Roles userRole2 = Roles.ROLE_ADMIN;
        Roles userRole3 = Roles.ROLE_FINANCIAL;
        Boolean isActive = false;
        boolean isActive1 = true;
        boolean isActive2 = false;
        boolean isActive3 = true;
        List<Roles> roles = Arrays.stream(Roles.values()).toList();
        requester.setRole(requesterRole);

        User user1 = User.builder()
                .id(userId + 1).firstName("1" + userFirstName).email("1" + email).role(userRole1).isActive(isActive1)
                .build();
        User user2 = User.builder()
                .id(userId + 2).firstName("2" + userFirstName).email("2" + email).role(userRole2).isActive(isActive2)
                .build();
        User user3 = User.builder()
                .id(userId + 3).firstName("3" + userFirstName).email("3" + email).role(userRole3).isActive(isActive3)
                .build();

        List<User> userList = List.of(user2);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(user1.getRole())
                .isActive(user1.getIsActive())
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(user2.getRole())
                .isActive(user2.getIsActive())
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(user3.getRole())
                .isActive(user3.getIsActive())
                .build();

        List<UserDto> userDtoList = List.of(userDto2);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findAllByRoleInAndIsActive(roles, isActive)).thenReturn(Optional.of(userList));
        when(userMapper.map(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId, isActive);

        assertAll(
                () -> assertEquals(userDtoList, returnedUsersDto),
                () -> verify(userRepository).findAllByRoleInAndIsActive(roles, isActive),
                () -> verify(userMapper).map(userList)
        );
    }

    @Test
    void getAllUsers_whenAdminGetAllUsers_returnAllUsersThanBoss() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole1 = Roles.ROLE_ADMIN;
        Roles userRole2 = Roles.ROLE_BOSS;
        Roles userRole3 = Roles.ROLE_FINANCIAL;
        Boolean isActive = null;
        boolean isActive1 = true;
        boolean isActive2 = false;
        boolean isActive3 = true;
        List<Roles> roles = Arrays.asList(Roles.values()).subList(requesterRole.ordinal(), Roles.values().length);
        requester.setRole(requesterRole);
        User user1 = User.builder()
                .id(userId + 1).firstName("1" + userFirstName).email("1" + email).role(userRole1).isActive(isActive1)
                .build();
        User user2 = User.builder()
                .id(userId + 2).firstName("2" + userFirstName).email("2" + email).role(userRole2).isActive(isActive2)
                .build();
        User user3 = User.builder()
                .id(userId + 3).firstName("3" + userFirstName).email("3" + email).role(userRole3).isActive(isActive3)
                .build();

        List<User> userList = List.of(user1, user3);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(user1.getRole())
                .isActive(user1.getIsActive())
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(user2.getRole())
                .isActive(user2.getIsActive())
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(user3.getRole())
                .isActive(user3.getIsActive())
                .build();

        List<UserDto> userDtoList = List.of(userDto1, userDto3);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findAllByRoleIn(roles)).thenReturn(Optional.of(userList));
        when(userMapper.map(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId, isActive);

        assertAll(
                () -> assertEquals(userDtoList, returnedUsersDto),
                () -> verify(userRepository).findAllByRoleIn(roles),
                () -> verify(userMapper).map(userList)
        );
    }

    @Test
    void getAllUsers_whenAdminGetAllActiveUsers_returnAllActiveUsersThanBoss() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole1 = Roles.ROLE_ADMIN;
        Roles userRole2 = Roles.ROLE_FINANCIAL;
        Roles userRole3 = Roles.ROLE_BOSS;
        Boolean isActive = true;
        boolean isActive1 = true;
        boolean isActive2 = false;
        boolean isActive3 = true;
        List<Roles> roles = Arrays.asList(Roles.values()).subList(requesterRole.ordinal(), Roles.values().length);
        requester.setRole(requesterRole);
        User user1 = User.builder()
                .id(userId + 1).firstName("1" + userFirstName).email("1" + email).role(userRole1).isActive(isActive1)
                .build();
        User user2 = User.builder()
                .id(userId + 2).firstName("2" + userFirstName).email("2" + email).role(userRole2).isActive(isActive2)
                .build();
        User user3 = User.builder()
                .id(userId + 3).firstName("3" + userFirstName).email("3" + email).role(userRole3).isActive(isActive3)
                .build();

        List<User> userList = List.of(user1);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(user1.getRole())
                .isActive(user1.getIsActive())
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(user2.getRole())
                .isActive(user2.getIsActive())
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(user3.getRole())
                .isActive(user3.getIsActive())
                .build();

        List<UserDto> userDtoList = List.of(userDto1);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findAllByRoleInAndIsActive(roles, isActive)).thenReturn(Optional.of(userList));
        when(userMapper.map(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId, isActive);

        assertAll(
                () -> assertEquals(userDtoList, returnedUsersDto),
                () -> verify(userRepository).findAllByRoleInAndIsActive(roles, isActive),
                () -> verify(userMapper).map(userList)
        );
    }

    @Test
    void getAllUsers_whenAdminGetAllNotActiveUsers_returnAllNotActiveUsersThanBoss() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole1 = Roles.ROLE_ADMIN;
        Roles userRole2 = Roles.ROLE_FINANCIAL;
        Roles userRole3 = Roles.ROLE_BOSS;
        Boolean isActive = true;
        boolean isActive1 = true;
        boolean isActive2 = false;
        boolean isActive3 = true;
        List<Roles> roles = Arrays.asList(Roles.values()).subList(requesterRole.ordinal(), Roles.values().length);
        requester.setRole(requesterRole);
        User user1 = User.builder()
                .id(userId + 1).firstName("1" + userFirstName).email("1" + email).role(userRole1).isActive(isActive1)
                .build();
        User user2 = User.builder()
                .id(userId + 2).firstName("2" + userFirstName).email("2" + email).role(userRole2).isActive(isActive2)
                .build();
        User user3 = User.builder()
                .id(userId + 3).firstName("3" + userFirstName).email("3" + email).role(userRole3).isActive(isActive3)
                .build();

        List<User> userList = List.of(user2);

        UserDto userDto1 = UserDto.builder()
                .id(user1.getId()).firstName(user1.getFirstName()).email(user1.getEmail()).role(user1.getRole())
                .isActive(user1.getIsActive())
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(user2.getId()).firstName(user2.getFirstName()).email(user2.getEmail()).role(user2.getRole())
                .isActive(user2.getIsActive())
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(user3.getId()).firstName(user3.getFirstName()).email(user3.getEmail()).role(user3.getRole())
                .isActive(user3.getIsActive())
                .build();

        List<UserDto> userDtoList = List.of(userDto2);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findAllByRoleInAndIsActive(roles, isActive)).thenReturn(Optional.of(userList));
        when(userMapper.map(userList)).thenReturn(userDtoList);

        List<UserDto> returnedUsersDto = userService.getAllUsers(requesterId, isActive);

        assertAll(
                () -> assertEquals(userDtoList, returnedUsersDto),
                () -> verify(userRepository).findAllByRoleInAndIsActive(roles, isActive),
                () -> verify(userMapper).map(userList)
        );
    }

    @Test
    void getAllUsers_whenUserGetAllUsers_ThenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(AccessDeniedException.class,
                () -> userService.getAllUsers(requesterId, isActive));
    }

    @Test
    void getAllUsers_whenFinancialGetAllUsers_ThenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_FINANCIAL;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(AccessDeniedException.class,
                () -> userService.getAllUsers(requesterId, isActive));
    }

    @Test
    void getAllUsers_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getAllUsers(requesterId, isActive));
    }

    @Test
    void deleteUserById_whenRequesterBoss_thenUserWillDelete() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.deleteUserById(user.getId())).thenReturn(1);

        userService.deleteUserById(requesterId, userId);

        verify(userRepository).deleteUserById(longArgumentCaptor.capture());
        Long idForDelete = longArgumentCaptor.getValue();
        assertEquals(userId, idForDelete);
    }

    @Test
    void deleteUserById_whenAdminDeleteUser_thenUserWillDelete() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.deleteUserById(user.getId())).thenReturn(1);

        userService.deleteUserById(requesterId, userId);

        verify(userRepository).deleteUserById(longArgumentCaptor.capture());
        Long idForDelete = longArgumentCaptor.getValue();
        assertEquals(userId, idForDelete);
    }

    @Test
    void deleteUserById_whenAdminDeleteBoss_thenAccessDenied() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> userService.deleteUserById(requesterId, userId));
    }

    @Test
    void deleteUserById_whenUserDeleteSmb_thenAccessDenied() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles userRole = Roles.ROLE_FINANCIAL;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> userService.deleteUserById(requesterId, userId));
    }

    @Test
    void deleteUserById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.deleteUserById(requesterId, userId));
    }

    @Test
    void setUserState_whenBossDeactivatedAdmin_thenAdminWillDeactivated() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_ADMIN;
        Boolean newActive = false;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .isActive(true)
                .build();

        User userAfter = User.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .isActive(newActive)
                .build();

        UserDto userAfterDto = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userAfterDto);

        UserDto resultUserDto = userService.setUserState(requesterId, userId, newActive);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userRepository).save(user),
                () -> verify(userMapper).toUserDto(userAfter),
                () -> assertEquals(newActive, userForSave.getIsActive()),
                () -> assertEquals(resultUserDto, userAfterDto)
        );
    }

    @Test
    void setUserState_whenBossActivatedAdmin_thenAdminWillActivated() {
        Roles requesterRole = Roles.ROLE_BOSS;
        Roles userRole = Roles.ROLE_ADMIN;
        Boolean newActive = true;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .isActive(false)
                .build();

        User userAfter = User.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .isActive(newActive)
                .build();

        UserDto userAfterDto = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userAfterDto);

        UserDto resultUserDto = userService.setUserState(requesterId, userId, newActive);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userRepository).save(user),
                () -> verify(userMapper).toUserDto(userAfter),
                () -> assertEquals(newActive, userForSave.getIsActive()),
                () -> assertEquals(resultUserDto, userAfterDto)
        );
    }

    @Test
    void setUserState_whenAdminDeactivatedUser_thenUserWillDeactivated() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_USER;
        Boolean newActive = false;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .isActive(true)
                .build();

        User userAfter = User.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .isActive(newActive)
                .build();

        UserDto userAfterDto = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userAfterDto);

        UserDto resultUserDto = userService.setUserState(requesterId, userId, newActive);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userRepository).save(user),
                () -> verify(userMapper).toUserDto(userAfter),
                () -> assertEquals(newActive, userForSave.getIsActive()),
                () -> assertEquals(resultUserDto, userAfterDto)
        );
    }

    @Test
    void setUserState_whenAdminActivatedUser_thenUserWillActivated() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_USER;
        Boolean newActive = true;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .isActive(false)
                .build();

        User userAfter = User.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .isActive(newActive)
                .build();

        UserDto userAfterDto = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userAfterDto);

        UserDto resultUserDto = userService.setUserState(requesterId, userId, newActive);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userRepository).save(user),
                () -> verify(userMapper).toUserDto(userAfter),
                () -> assertEquals(newActive, userForSave.getIsActive()),
                () -> assertEquals(resultUserDto, userAfterDto)
        );
    }

    @Test
    void setUserState_whenAdminDeactivatedFinancial_thenFinancialWillDeactivated() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_FINANCIAL;
        Boolean newActive = false;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .isActive(true)
                .build();

        User userAfter = User.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .isActive(newActive)
                .build();

        UserDto userAfterDto = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userAfterDto);

        UserDto resultUserDto = userService.setUserState(requesterId, userId, newActive);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userRepository).save(user),
                () -> verify(userMapper).toUserDto(userAfter),
                () -> assertEquals(newActive, userForSave.getIsActive()),
                () -> assertEquals(resultUserDto, userAfterDto)
        );
    }

    @Test
    void setUserState_whenAdminActivatedFinancial_thenFinancialWillActivated() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_FINANCIAL;
        Boolean newActive = true;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .isActive(false)
                .build();

        User userAfter = User.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .isActive(newActive)
                .build();

        UserDto userAfterDto = UserDto.builder()
                .id(userAfter.getId())
                .lastName(userAfter.getLastName())
                .firstName(userAfter.getFirstName())
                .middleName(userAfter.getMiddleName())
                .email(userAfter.getEmail())
                .role(userAfter.getRole())
                .password(userAfter.getPassword())
                .isActive(userAfter.getIsActive())
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(userAfter);
        when(userMapper.toUserDto(userAfter)).thenReturn(userAfterDto);

        UserDto resultUserDto = userService.setUserState(requesterId, userId, newActive);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userForSave = userArgumentCaptor.getValue();

        assertAll(
                () -> verify(userRepository).findById(requesterId),
                () -> verify(userRepository).findById(userId),
                () -> verify(userRepository).save(user),
                () -> verify(userMapper).toUserDto(userAfter),
                () -> assertEquals(newActive, userForSave.getIsActive()),
                () -> assertEquals(resultUserDto, userAfterDto)
        );
    }

    @Test
    void setUserState_whenAdminChangeBoss_thenAccessDenied() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        Roles userRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> userService.setUserState(requesterId, userId, true));
    }

    @Test
    void setUserState_whenUserChangeSmb_thenAccessDenied() {
        Roles requesterRole = Roles.ROLE_USER;
        Roles userRole = Roles.ROLE_FINANCIAL;
        requester.setRole(requesterRole);

        User user = User.builder()
                .id(userId)
                .firstName(userFirstName)
                .email(email)
                .role(userRole)
                .build();

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> userService.setUserState(requesterId, userId, true));
    }

    @Test
    void setUserState_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.setUserState(requesterId, userId, true));
    }
}