package ru.modgy.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.modgy.user.controller.UserController;
import ru.modgy.user.dto.NewUserDto;
import ru.modgy.user.dto.UpdateUserDto;
import ru.modgy.user.dto.UserDto;
import ru.modgy.user.model.Roles;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserIntegrationTest {
    @Autowired
    private final UserController userController;

    final String userFirstName = "Иван";
    final String userMiddleName = "Батькович";
    final String userLastName = "Мартынов";
    final String userEmail = "martyn@mail.com";
    final String userPassword = "user_pwd";
    final Roles userRole = Roles.ROLE_USER;
    final boolean isActive = true;

    NewUserDto newUserDto = new NewUserDto(userLastName, userFirstName, userMiddleName, userPassword, userEmail,
            userRole, isActive);

    @Test
    @Order(1)
    void context() {
        assertAll(
                () -> assertNotNull(userController));
    }

    @Test
    @Order(2)
    void getUserById() {
        UserDto userById = userController.getUserById(1L, 1L);
        assertAll(
                () -> assertNotNull(userById),
                () -> assertEquals(userById.getId(), 1L),
                () -> assertEquals(userById.getFirstName(), "boss"),
                () -> assertEquals(userById.getEmail(), "boss@mail.ru"),
                () -> assertEquals(userById.getPassword(), "boss_pwd"),
                () -> assertEquals(userById.getRole(), Roles.ROLE_BOSS),
                () -> assertTrue(userById.getIsActive())
        );
    }

    @Test
    @Order(3)
    void addUser() {
        UserDto addedUser = userController.addUser(1L, newUserDto);

        assertDoesNotThrow(() -> userController.getUserById(1L, addedUser.getId()));

        UserDto userById = userController.getUserById(1L, addedUser.getId());

        assertEquals(addedUser, userById);
    }

    @Test
    @Order(4)
    void getAllUsers() {
        final Roles userRole1 = Roles.ROLE_USER;
        final Roles userRole2 = Roles.ROLE_ADMIN;
        final Roles userRole3 = Roles.ROLE_FINANCIAL;
        final boolean isActive1 = false;
        final boolean isActive2 = false;
        final boolean isActive3 = false;

        NewUserDto newUserDto1 = new NewUserDto(userLastName + "1", userFirstName + "1",
                userMiddleName + "1", userPassword + "1", "1" + userEmail,
                userRole1, isActive1);
        NewUserDto newUserDto2 = new NewUserDto(userLastName + "2", userFirstName + "2",
                userMiddleName + "2", userPassword + "2", "2" + userEmail,
                userRole2, isActive2);
        NewUserDto newUserDto3 = new NewUserDto(userLastName + "3", userFirstName + "3",
                userMiddleName + "3", userPassword + "3", "3" + userEmail,
                userRole3, isActive3);

        UserDto userDto1 = userController.addUser(1L, newUserDto1);
        UserDto userDto2 = userController.addUser(1L, newUserDto2);
        UserDto userDto3 = userController.addUser(1L, newUserDto3);


        Collection<UserDto> allUsers = userController.getAllUsers(1L, false);

        assertAll(
                () -> assertEquals(3, allUsers.size()),
                () -> assertThat(allUsers, hasItem(userDto1)),
                () -> assertThat(allUsers, hasItem(userDto2)),
                () -> assertThat(allUsers, hasItem(userDto3))
        );
    }

    @Test
    @Order(5)
    void updateUser() {
        NewUserDto newUserBeforeDto = NewUserDto.builder()
                .firstName("Jesse")
                .lastName("Pinkman")
                .password("beach")
                .email("pinkman@mail.ru")
                .role(Roles.ROLE_USER)
                .build();

        UserDto addedUser = userController.addUser(1L, newUserBeforeDto);

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("pinkman@albuquerque.ru")
                .build();

        UserDto updatedUserDto = userController.updateUser(1L, addedUser.getId(), updateUserDto);

        UserDto userAfter = userController.getUserById(1L, updatedUserDto.getId());

        assertAll(
                () -> assertNotNull(userAfter),
                () -> assertEquals(userAfter.getId(), updatedUserDto.getId()),
                () -> assertEquals(userAfter.getFirstName(), addedUser.getFirstName()),
                () -> assertEquals(userAfter.getLastName(), addedUser.getLastName()),
                () -> assertEquals(userAfter.getEmail(), updateUserDto.getEmail()),
                () -> assertEquals(userAfter.getPassword(), addedUser.getPassword()),
                () -> assertEquals(userAfter.getRole(), addedUser.getRole()),
                () -> assertEquals(userAfter.getIsActive(), addedUser.getIsActive())
        );
    }

    @Test
    @Order(6)
    void deleteUserById() {
        NewUserDto heisenbergNewDto = NewUserDto.builder()
                .firstName("Walter")
                .lastName("White")
                .password("breaking")
                .email("heisenberg@mail.ru")
                .role(Roles.ROLE_ADMIN)
                .build();

        UserDto heisenbergDto = userController.addUser(1L, heisenbergNewDto);

        Collection<UserDto> allUsers5 = userController.getAllUsers(1L, null);

        assertThat(allUsers5, hasItem(heisenbergDto));

        userController.deleteUserById(1L, heisenbergDto.getId());

        allUsers5 = userController.getAllUsers(1L, null);

        assertThat(allUsers5, not(hasItem(heisenbergDto)));
    }

    @Test
    @Order(7)
    void setUserState() {
        NewUserDto gustavoNewDto = NewUserDto.builder()
                .firstName("Gustavo")
                .lastName("Fring")
                .password("hermanos")
                .email("los@pollos.ru")
                .role(Roles.ROLE_FINANCIAL)
                .build();

        UserDto gustavoDto = userController.addUser(1L, gustavoNewDto);

        UserDto trueActiveGusDtoById = userController.getUserById(1L, gustavoDto.getId());

        assertTrue(trueActiveGusDtoById.getIsActive());

        userController.setUserState(1L, trueActiveGusDtoById.getId(), false);

        UserDto falseActiveGusDtoById = userController.getUserById(1L, gustavoDto.getId());

        assertFalse(falseActiveGusDtoById.getIsActive());

    }
}
