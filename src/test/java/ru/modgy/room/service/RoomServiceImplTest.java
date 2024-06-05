package ru.modgy.room.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.model.Pet;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.dto.mapper.CategoryMapper;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;
import ru.modgy.room.dto.mapper.RoomMapper;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.utility.EntityService;
import ru.modgy.utility.UtilityService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoomServiceImplTest {
    private final LocalDate checkIn = LocalDate.of(2024, 1, 1);
    private final LocalDate checkOut = LocalDate.of(2024, 1, 2);
    private final User boss = User.builder()
            .email("boss@pethotel.ru")
            .id(1L)
            .firstName("boss")
            .role(Roles.ROLE_BOSS)
            .build();

    final Category category = Category.builder()
            .id(1L)
            .name("name")
            .description("description")
            .build();

    final CategoryDto categoryDto = CategoryDto.builder()
            .id(1L)
            .name("name")
            .description("description")
            .build();

    private final Room room = Room.builder()
            .id(1L)
            .area(5.0)
            .number("standard room")
            .category(new Category(1L, "name", "description"))
            .isVisible(true)
            .build();

    private final RoomDto roomDto = RoomDto.builder()
            .id(1L)
            .area(5.0)
            .number("standard room")
            .categoryDto(new CategoryDto(1L, "name", "description"))
            .isVisible(true)
            .build();

    private final NewRoomDto newRoomDto = NewRoomDto.builder()
            .area(5.0)
            .number("standard room")
            .categoryId(1L)
            .isVisible(true)
            .build();

    private final Room hiddenRoom = Room.builder()
            .id(1L)
            .area(5.0)
            .number("standard room")
            .category(new Category(1L, "name", "description"))
            .isVisible(false)
            .build();

    private final RoomDto hiddenRoomDto = RoomDto.builder()
            .id(1L)
            .area(5.0)
            .number("standard room")
            .categoryDto(new CategoryDto(1L, "name", "description"))
            .isVisible(false)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(checkIn)
            .checkOutDate(checkOut)
            .status(StatusBooking.STATUS_INITIAL)
            .price(0.0)
            .amount(0.0)
            .prepaymentAmount(0.0)
            .isPrepaid(false)
            .room(room)
            .pets(List.of(new Pet()))
            .build();

    @InjectMocks
    private RoomServiceImpl roomService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UtilityService utilityService;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private EntityService entityService;

    @Test
    void addRoom_whenAddRoomByBoss_thenRoomAdded() {
        when(entityService.getCategoryIfExists(anyLong())).thenReturn(category);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoom(any(NewRoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.addRoom(boss.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(roomDto.getArea(), result.getArea());
        Assertions.assertTrue(result.getIsVisible());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).save(any(Room.class));
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomById_whenGetRoomByBoss_thenReturnedRoom() {
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.getRoomById(boss.getId(), room.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(roomDto.getArea(), result.getArea());
        Assertions.assertTrue(result.getIsVisible());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());

        verify(entityService, times(1)).getRoomIfExists(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void updateRoom_whenRequesterBossAndRoomFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .area(10.0)
                .number("new standard room")
                .categoryId(1L)
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .area(10.0)
                .number("new standard room")
                .categoryDto(new CategoryDto(1L, "name", "description"))
                .isVisible(false)
                .build();

        Room newRoom = Room.builder()
                .area(10.0)
                .number("new standard room")
                .category(new Category(1L, "name", "description"))
                .isVisible(false)
                .build();

        when(entityService.getCategoryIfExists(anyLong())).thenReturn(category);
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(boss.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(updatedRoomDto.getArea(), result.getArea());
        Assertions.assertFalse(result.getIsVisible());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_whenRequesterBossAndRoomFoundAndAreaNewFieldNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .area(10.0)
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .area(10.0)
                .number("standard room")
                .categoryDto(new CategoryDto(1L, "name", "description"))
                .isVisible(true)
                .build();

        Room updatedRoom = Room.builder()
                .build();

        Room newRoom = Room.builder()
                .area(10.0)
                .number("standard room")
                .category(new Category(1L, "name", "description"))
                .isVisible(true)
                .build();

        when(entityService.getCategoryIfExists(anyLong())).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(updatedRoom);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(boss.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(updatedRoomDto.getArea(), result.getArea());
        Assertions.assertTrue(result.getIsVisible());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_whenRequesterBossAndRoomFoundAndNumberNewFieldNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .number("new standard room")
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .area(5.0)
                .number("new standard room")
                .categoryDto(new CategoryDto(1L, "name", "description"))
                .isVisible(false)
                .build();

        Room updatedRoom = Room.builder()
                .isVisible(false)
                .build();

        Room newRoom = Room.builder()
                .area(5.0)
                .number("standard room")
                .category(new Category(1L, "name", "description"))
                .isVisible(false)
                .build();

        when(entityService.getCategoryIfExists(anyLong())).thenReturn(category);
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(updatedRoom);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(boss.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(updatedRoomDto.getArea(), result.getArea());
        Assertions.assertFalse(result.getIsVisible());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void getAllRooms_whenGetAllRoomsByBossAndTrue_thenReturnAllRooms() {
        when(roomRepository.getAllRooms(anyBoolean())).thenReturn(Optional.of(List.of(room)));
        when(roomMapper.toListRoomDto(any())).thenReturn(List.of(roomDto));

        Collection<RoomDto> resultCollection = roomService.getAllRooms(boss.getId(), room.getIsVisible());
        List<RoomDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(roomDto.getCategoryDto(), result.get(0).getCategoryDto());
        Assertions.assertEquals(roomDto.getArea(), result.get(0).getArea());
        Assertions.assertTrue(result.get(0).getIsVisible());
        Assertions.assertEquals(roomDto.getNumber(), result.get(0).getNumber());

        verify(roomRepository, times(1)).getAllRooms(anyBoolean());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void hideRoomById_whenNoFutureBookings_thenRoomHidden() {
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(hiddenRoom);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(hiddenRoomDto);

        RoomDto result = roomService.hideRoomById(boss.getId(), hiddenRoom.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(hiddenRoomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(hiddenRoomDto.getArea(), result.getArea());
        Assertions.assertFalse(result.getIsVisible());
        Assertions.assertEquals(hiddenRoomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void hideRoomById_whenFutureBookingsExist_thenConflictException() {
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(bookingRepository.findFutureBookingsForRoom(anyLong(), any())).thenReturn(Optional.of(List.of(booking)));

        assertThrows(ConflictException.class, () -> roomService.hideRoomById(boss.getId(), room.getId()));

        verify(roomRepository, times(0)).save(any(Room.class));
    }

    @Test
    void unhideRoomById_whenUnhideRoomByIdByBoss_thenRoomUnhidden() {
        when(entityService.getRoomIfExists(anyLong())).thenReturn(hiddenRoom);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.unhideRoomById(boss.getId(), room.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getCategoryDto(), result.getCategoryDto());
        Assertions.assertEquals(roomDto.getArea(), result.getArea());
        Assertions.assertTrue(result.getIsVisible());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void permanentlyDeleteRoomById_whenRequesterBossAndRoomFound_thenRoomWillDelete() {
        when(roomRepository.deleteRoomById(anyLong())).thenReturn(1);

        roomService.permanentlyDeleteRoomById(boss.getId(), room.getId());

        verify(roomRepository, times(1)).deleteRoomById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void permanentlyDeleteRoomById_whenRequesterNotFound_thenNotFoundException() {
        doThrow(new NotFoundException(String.format("User with id=%d is not found", 66L)))
                .when(utilityService).checkBossAdminAccess(anyLong());

        assertThrows(NotFoundException.class,
                () -> roomService.permanentlyDeleteRoomById(boss.getId(), room.getId()));
    }

    @Test
    void permanentlyDeleteRoomById_whenRoomNotFound_thenNotFoundException() {
        when(roomRepository.deleteRoomById(anyLong())).thenReturn(0);

        assertThrows(NotFoundException.class,
                () -> roomService.permanentlyDeleteRoomById(boss.getId(), room.getId()));
    }

    @Test
    void getAvailableRoomsByCategoryInDates_whenOneAvailableRoom_thenReturnedListOfOneRoom() {
        when(roomRepository.findAvailableRoomsByCategoryInDates(
                anyLong(), any(), any())).thenReturn(Optional.of(List.of(room)));
        when(roomMapper.toListRoomDto(any())).thenReturn(List.of(roomDto));

        List<RoomDto> result = roomService.getAvailableRoomsByCategoryInDates(boss.getId(), category.getId(), checkIn, checkOut);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(roomDto.getCategoryDto(), result.get(0).getCategoryDto());
        Assertions.assertEquals(roomDto.getArea(), result.get(0).getArea());
        Assertions.assertTrue(result.get(0).getIsVisible());
        Assertions.assertEquals(roomDto.getNumber(), result.get(0).getNumber());

        verify(roomRepository, times(1)).findAvailableRoomsByCategoryInDates(anyLong(), any(), any());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getAvailableRoomsByCategoryInDates_whenNoAvailableRoom_thenReturnedEmptyList() {
        when(roomRepository.findAvailableRoomsByCategoryInDates(
                anyLong(), any(), any())).thenReturn(Optional.empty());
        when(roomMapper.toListRoomDto(any())).thenReturn(new ArrayList<>());

        List<RoomDto> result = roomService.getAvailableRoomsByCategoryInDates(boss.getId(), category.getId(), checkIn, checkOut);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());

        verify(roomRepository, times(1)).findAvailableRoomsByCategoryInDates(anyLong(), any(), any());
        verifyNoMoreInteractions(roomRepository);
    }
}
