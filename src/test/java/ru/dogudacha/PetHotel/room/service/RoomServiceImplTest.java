package ru.dogudacha.PetHotel.room.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.RoomWithoutPriceDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.dto.mapper.RoomMapper;
import ru.dogudacha.PetHotel.room.model.Room;
import ru.dogudacha.PetHotel.room.model.RoomTypes;
import ru.dogudacha.PetHotel.room.repository.RoomRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoomServiceImplTest {
    private final User boss = User.builder()
            .email("boss@pethotel.ru")
            .id(1L)
            .name("boss")
            .role(Roles.ROLE_BOSS)
            .build();

    private final User admin = User.builder()
            .email("admin@pethotel.ru")
            .id(2L)
            .name("admin")
            .role(Roles.ROLE_ADMIN)
            .build();

    private final User user = User.builder()
            .email("user@pethotel.ru")
            .id(2L)
            .name("user")
            .role(Roles.ROLE_USER)
            .build();

    private final User financial = User.builder()
            .email("financial@pethotel.ru")
            .id(2L)
            .name("financial")
            .role(Roles.ROLE_FINANCIAL)
            .build();

    private final Room room = Room.builder()
            .id(1L)
            .size(5.0)
            .price(1000.0)
            .number("standard room")
            .type(RoomTypes.STANDARD)
            .isAvailable(true)
            .build();

    private final RoomDto roomDto = RoomDto.builder()
            .id(1L)
            .size(5.0)
            .price(1000.0)
            .number("standard room")
            .type(RoomTypes.STANDARD)
            .isAvailable(true)
            .build();

    private final RoomWithoutPriceDto roomWithoutPriceDto = RoomWithoutPriceDto.builder()
            .id(1L)
            .size(5.0)
            .number("standard room")
            .type(RoomTypes.STANDARD)
            .isAvailable(true)
            .build();

    @InjectMocks
    private RoomServiceImpl roomService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomMapper roomMapper;


    @Test
    void addRoom_whenAddRoomByBoss_thenRoomAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.addRoom(boss.getId(), roomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getType(), result.getType());
        Assertions.assertEquals(roomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).save(any(Room.class));
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void addRoom_whenAddRoomByAdmin_thenRoomAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.addRoom(admin.getId(), roomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getType(), result.getType());
        Assertions.assertEquals(roomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).save(any(Room.class));
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void addRoom_whenAddRoomByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> roomService.addRoom(user.getId(), roomDto));
    }

    @Test
    void addRoom_whenAddRoomByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> roomService.addRoom(financial.getId(), roomDto));
    }

    @Test
    void getRoomById_whenGetRoomByBoss_thenReturnedRoom() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.getRoomById(boss.getId(), room.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getType(), result.getType());
        Assertions.assertEquals(roomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomById_whenGetRoomByAdmin_thenReturnedRoom() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.getRoomById(admin.getId(), room.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getType(), result.getType());
        Assertions.assertEquals(roomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomById_whenGetRoomByFinancial_thenReturnedRoom() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(roomDto);

        RoomDto result = roomService.getRoomById(financial.getId(), room.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getType(), result.getType());
        Assertions.assertEquals(roomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomById_whenGetRoomByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> roomService.getRoomById(user.getId(), room.getId()));
    }

    @Test
    void getRoomById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roomService.getRoomById(boss.getId(), room.getId()));
    }

    @Test
    void getRoomWithoutPriceById_whenGetRoomWithoutPriceByUser_thenReturnedRoom() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomMapper.toRoom(any(RoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDtoWithoutPrice(any(Room.class))).thenReturn(roomWithoutPriceDto);

        RoomWithoutPriceDto result = roomService.getRoomWithoutPriceById(user.getId(), room.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(roomDto.getType(), result.getType());
        Assertions.assertEquals(roomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.getNumber());

        verify(roomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomWithoutPriceById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roomService.getRoomWithoutPriceById(user.getId(), room.getId()));
    }

    @Test
    void updateRoom_whenRequesterBossAndRoomFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .id(1L)
                .size(10.0)
                .price(2000.0)
                .number("new standard room")
                .type(RoomTypes.LUX)
                .isAvailable(false)
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .size(10.0)
                .price(2000.0)
                .number("new standard room")
                .type(RoomTypes.LUX)
                .isAvailable(false)
                .build();

        Room newRoom = Room.builder()
                .size(10.0)
                .price(2000.0)
                .number("new standard room")
                .type(RoomTypes.LUX)
                .isAvailable(false)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(boss.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getType(), result.getType());
        Assertions.assertEquals(updatedRoomDto.getSize(), result.getSize());
        Assertions.assertFalse(result.getIsAvailable());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(updatedRoomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_whenRequesterBossAndRoomFoundAndPriceNewFieldNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .price(2000.0)
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .size(5.0)
                .price(2000.0)
                .number("standard room")
                .type(RoomTypes.STANDARD)
                .isAvailable(true)
                .build();

        Room updatedRoom = Room.builder()
                .price(2000.0)
                .build();

        Room newRoom = Room.builder()
                .size(5.0)
                .price(2000.0)
                .number("standard room")
                .type(RoomTypes.STANDARD)
                .isAvailable(true)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(updatedRoom);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(boss.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getType(), result.getType());
        Assertions.assertEquals(updatedRoomDto.getSize(), result.getSize());
        Assertions.assertTrue(result.getIsAvailable());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(updatedRoomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_whenRequesterBossAndRoomFoundAndAvailableNewFieldNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .isAvailable(false)
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .size(5.0)
                .price(1000.0)
                .number("standard room")
                .type(RoomTypes.STANDARD)
                .isAvailable(false)
                .build();

        Room updatedRoom = Room.builder()
                .isAvailable(false)
                .build();

        Room newRoom = Room.builder()
                .size(5.0)
                .price(1000.0)
                .number("standard room")
                .type(RoomTypes.STANDARD)
                .isAvailable(false)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(updatedRoom);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(boss.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getType(), result.getType());
        Assertions.assertEquals(updatedRoomDto.getSize(), result.getSize());
        Assertions.assertFalse(result.getIsAvailable());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(updatedRoomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_whenRequesterAdminAndRoomFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        UpdateRoomDto newRoomDto = UpdateRoomDto.builder()
                .id(1L)
                .size(10.0)
                .price(2000.0)
                .number("new standard room")
                .type(RoomTypes.LUX)
                .isAvailable(false)
                .build();

        RoomDto updatedRoomDto = RoomDto.builder()
                .id(1L)
                .size(10.0)
                .price(2000.0)
                .number("new standard room")
                .type(RoomTypes.LUX)
                .isAvailable(false)
                .build();

        Room newRoom = Room.builder()
                .size(10.0)
                .price(2000.0)
                .number("new standard room")
                .type(RoomTypes.LUX)
                .isAvailable(false)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn((newRoom));
        when(roomMapper.toRoom(any(UpdateRoomDto.class))).thenReturn(room);
        when(roomMapper.toRoomDto(any(Room.class))).thenReturn(updatedRoomDto);

        RoomDto result = roomService.updateRoom(admin.getId(), room.getId(), newRoomDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedRoomDto.getType(), result.getType());
        Assertions.assertEquals(updatedRoomDto.getSize(), result.getSize());
        Assertions.assertFalse(result.getIsAvailable());
        Assertions.assertEquals(updatedRoomDto.getNumber(), result.getNumber());
        Assertions.assertEquals(updatedRoomDto.getPrice(), result.getPrice());

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void updateRoom_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roomService.updateRoom(boss.getId(), room.getId(), new UpdateRoomDto()));
    }

    @Test
    void updateRoom_whenRequesterFoundAndRoomNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roomService.updateRoom(boss.getId(), room.getId(), new UpdateRoomDto()));
    }

    @Test
    void updateRoom_whenUpdateRoomByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> roomService.updateRoom(user.getId(), room.getId(), new UpdateRoomDto()));
    }

    @Test
    void updateRoom_whenUpdateRoomByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> roomService.updateRoom(financial.getId(), room.getId(), new UpdateRoomDto()));
    }

    @Test
    void getAllRooms_whenGetAllRoomsByBoss_thenReturnAllRooms() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.getAllRooms()).thenReturn(Optional.of(List.of(room)));
        when(roomMapper.toListRoomDto(anyList())).thenReturn(List.of(roomDto));

        Collection<RoomDto> resultCollection = roomService.getAllRooms(boss.getId());
        List<RoomDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(roomDto.getType(), result.get(0).getType());
        Assertions.assertEquals(roomDto.getSize(), result.get(0).getSize());
        Assertions.assertTrue(result.get(0).getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.get(0).getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.get(0).getPrice());

        verify(roomRepository, times(1)).getAllRooms();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getAllRooms_whenGetAllRoomsByAdmin_thenReturnAllRooms() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(roomRepository.getAllRooms()).thenReturn(Optional.of(List.of(room)));
        when(roomMapper.toListRoomDto(anyList())).thenReturn(List.of(roomDto));

        Collection<RoomDto> resultCollection = roomService.getAllRooms(admin.getId());
        List<RoomDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(roomDto.getType(), result.get(0).getType());
        Assertions.assertEquals(roomDto.getSize(), result.get(0).getSize());
        Assertions.assertTrue(result.get(0).getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.get(0).getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.get(0).getPrice());

        verify(roomRepository, times(1)).getAllRooms();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getAllRooms_whenGetAllRoomsByFinancial_thenReturnAllRooms() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));
        when(roomRepository.getAllRooms()).thenReturn(Optional.of(List.of(room)));
        when(roomMapper.toListRoomDto(anyList())).thenReturn(List.of(roomDto));

        Collection<RoomDto> resultCollection = roomService.getAllRooms(financial.getId());
        List<RoomDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(roomDto.getType(), result.get(0).getType());
        Assertions.assertEquals(roomDto.getSize(), result.get(0).getSize());
        Assertions.assertTrue(result.get(0).getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.get(0).getNumber());
        Assertions.assertEquals(roomDto.getPrice(), result.get(0).getPrice());

        verify(roomRepository, times(1)).getAllRooms();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getAllRooms_whenGetAllRoomsByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> roomService.getAllRooms(user.getId()));
    }

    @Test
    void getAllRoomsWithoutPrice_whenGetAllRoomsWithoutPriceByUser_thenReturnAllRoomsWithoutPrice() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roomRepository.getAllRooms()).thenReturn(Optional.of(List.of(room)));
        when(roomMapper.toListRoomWithoutPriceDto(anyList())).thenReturn(List.of(roomWithoutPriceDto));

        Collection<RoomWithoutPriceDto> resultCollection = roomService.getAllRoomsWithoutPrice(user.getId());
        List<RoomWithoutPriceDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(roomDto.getType(), result.get(0).getType());
        Assertions.assertEquals(roomDto.getSize(), result.get(0).getSize());
        Assertions.assertTrue(result.get(0).getIsAvailable());
        Assertions.assertEquals(roomDto.getNumber(), result.get(0).getNumber());

        verify(roomRepository, times(1)).getAllRooms();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void deleteRoomById_whenRequesterBossAndRoomFound_thenRoomWillDelete() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.deleteRoomById(anyLong())).thenReturn(1);

        roomService.deleteRoomById(boss.getId(), room.getId());

        verify(roomRepository, times(1)).deleteRoomById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void deleteRoomById_whenRequesterAdminAndRoomFound_thenRoomWillDelete() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(roomRepository.deleteRoomById(anyLong())).thenReturn(1);

        roomService.deleteRoomById(admin.getId(), room.getId());

        verify(roomRepository, times(1)).deleteRoomById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void deleteRoomById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roomService.deleteRoomById(boss.getId(), room.getId()));
    }

    @Test
    void deleteRoomById_whenRoomNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.deleteRoomById(anyLong())).thenReturn(0);

        assertThrows(NotFoundException.class,
                () -> roomService.deleteRoomById(boss.getId(), room.getId()));
    }
}
