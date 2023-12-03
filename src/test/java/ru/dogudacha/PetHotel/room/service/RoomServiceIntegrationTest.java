package ru.dogudacha.PetHotel.room.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.model.Room;
import ru.dogudacha.PetHotel.room.model.RoomTypes;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class RoomServiceIntegrationTest {
    private final EntityManager em;
    private final RoomService service;

    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .name("admin")
            .role(Roles.ROLE_ADMIN)
            .build();

    final RoomDto roomDto = RoomDto.builder()
            .number("room number")
            .area(10.0)
            .type(RoomTypes.BIG)
            .description("room description")
            .isVisible(true)
            .build();

    final Room room = Room.builder()
            .number("room number")
            .area(10.0)
            .type(RoomTypes.BIG)
            .description("room description")
            .isVisible(true)
            .build();

    final Room hiddenRoom = Room.builder()
            .number("room number")
            .area(10.0)
            .type(RoomTypes.BIG)
            .description("room description")
            .isVisible(false)
            .build();

    final UpdateRoomDto updateRoomDto = UpdateRoomDto.builder()
            .number("update room number")
            .area(10.0)
            .type(RoomTypes.BIG)
            .description("update room description")
            .build();

    @Test
    void addRoom() {
        em.persist(requesterAdmin);
        RoomDto result = service.addRoom(requesterAdmin.getId(), roomDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getNumber(), equalTo(roomDto.getNumber()));
        assertThat(result.getArea(), equalTo(roomDto.getArea()));
        assertThat(result.getType(), equalTo(roomDto.getType()));
        assertThat(result.getDescription(), equalTo(roomDto.getDescription()));
        assertTrue(result.getIsVisible());
    }

    @Test
    void getRoomById() {
        em.persist(requesterAdmin);
        em.persist(room);
        RoomDto result = service.getRoomById(requesterAdmin.getId(), room.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getNumber(), equalTo(roomDto.getNumber()));
        assertThat(result.getArea(), equalTo(roomDto.getArea()));
        assertThat(result.getType(), equalTo(roomDto.getType()));
        assertThat(result.getDescription(), equalTo(roomDto.getDescription()));
        assertTrue(result.getIsVisible());
    }

    @Test
    void updateRoom() {
        em.persist(requesterAdmin);
        em.persist(room);
        RoomDto result = service.updateRoom(requesterAdmin.getId(), room.getId(), updateRoomDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getNumber(), equalTo(updateRoomDto.getNumber()));
        assertThat(result.getArea(), equalTo(updateRoomDto.getArea()));
        assertThat(result.getType(), equalTo(updateRoomDto.getType()));
        assertThat(result.getDescription(), equalTo(updateRoomDto.getDescription()));
        assertTrue(result.getIsVisible());
    }

    @Test
    void getAllRooms() {
        em.persist(requesterAdmin);
        em.persist(room);

        List<RoomDto> result = service.getAllRooms(requesterAdmin.getId(), room.getIsVisible()).stream().toList();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), notNullValue());
        assertThat(result.get(0).getNumber(), equalTo(roomDto.getNumber()));
        assertThat(result.get(0).getArea(), equalTo(roomDto.getArea()));
        assertThat(result.get(0).getType(), equalTo(roomDto.getType()));
        assertThat(result.get(0).getDescription(), equalTo(roomDto.getDescription()));
        assertTrue(result.get(0).getIsVisible());
    }

    @Test
    void hideRoom() {
        em.persist(requesterAdmin);
        em.persist(room);

        RoomDto result = service.hideRoomById(requesterAdmin.getId(), room.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getNumber(), equalTo(roomDto.getNumber()));
        assertThat(result.getArea(), equalTo(roomDto.getArea()));
        assertThat(result.getType(), equalTo(roomDto.getType()));
        assertThat(result.getDescription(), equalTo(roomDto.getDescription()));
        assertFalse(result.getIsVisible());
    }

    @Test
    void unhideRoom() {
        em.persist(requesterAdmin);
        em.persist(hiddenRoom);

        RoomDto result = service.unhideRoomById(requesterAdmin.getId(), hiddenRoom.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getNumber(), equalTo(roomDto.getNumber()));
        assertThat(result.getArea(), equalTo(roomDto.getArea()));
        assertThat(result.getType(), equalTo(roomDto.getType()));
        assertThat(result.getDescription(), equalTo(roomDto.getDescription()));
        assertTrue(result.getIsVisible());
    }

    @Test
    void permanentlyDeleteRoomById() {
        em.persist(requesterAdmin);
        em.persist(room);

        service.permanentlyDeleteRoomById(requesterAdmin.getId(), room.getId());

        String error = String.format("room with id=%d is not found", room.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getRoomById(requesterAdmin.getId(), room.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
