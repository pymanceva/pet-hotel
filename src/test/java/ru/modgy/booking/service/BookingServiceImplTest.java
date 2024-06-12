package ru.modgy.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.modgy.exception.ConflictException;
import ru.modgy.utility.EntityService;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.dto.mapper.BookingMapper;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.model.Room;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.utility.UtilityService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceImplTest {
    private final LocalDate checkIn = LocalDate.of(2024, 1, 1);
    private final LocalDate checkOut = LocalDate.of(2024, 1, 2);
    private final PetDto petDto = PetDto.builder()
            .id(1L)
            .type(TypeOfPet.DOG)
            .name("Шарик")
            .breed("Spaniel")
            .birthDate(LocalDate.of(2023, 1, 1))
            .sex(Sex.FEMALE)
            .build();
    private final Pet pet = Pet.builder()
            .id(1L)
            .type(TypeOfPet.DOG)
            .name("Шарик")
            .breed("Spaniel")
            .birthDate(LocalDate.of(2023, 1, 1))
            .sex(Sex.FEMALE)
            .build();
    private final User boss = User.builder()
            .email("boss@pethotel.ru")
            .id(1L)
            .firstName("boss")
            .role(Roles.ROLE_BOSS)
            .isActive(true)
            .build();
    private final User user = User.builder()
            .email("user@pethotel.ru")
            .id(2L)
            .firstName("user")
            .role(Roles.ROLE_USER)
            .isActive(true)
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
    private final NewBookingDto newBookingDto = NewBookingDto.builder()
            .type(TypesBooking.TYPE_BOOKING)
            .roomId(1L)
            .checkInDate(checkIn)
            .checkOutDate(checkOut)
            .petIds(List.of(1L))
            .build();
    private final Long bookingId = 1L;
    private final Booking booking = Booking.builder()
            .id(bookingId)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(checkIn)
            .checkOutDate(checkOut)
            .status(StatusBooking.STATUS_INITIAL)
            .price(0.0)
            .amount(0.0)
            .prepaymentAmount(0.0)
            .isPrepaid(false)
            .room(room)
            .pets(List.of(pet))
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(bookingId)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(checkIn)
            .checkOutDate(checkOut)
            .daysOfBooking(8)
            .status(StatusBooking.STATUS_INITIAL)
            .price(0.0)
            .amount(0.0)
            .prepaymentAmount(0.0)
            .isPrepaid(false)
            .room(roomDto)
            .pets(List.of(petDto))
            .build();
    private final UpdateBookingDto updateBookingDto = UpdateBookingDto.builder()
            .price(1000.00)
            .amount(7000.00)
            .prepaymentAmount(1000.00)
            .isPrepaid(true)
            .build();
    private final BookingDto updatedBookingDto = BookingDto.builder()
            .id(bookingId)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(checkIn)
            .checkOutDate(checkOut)
            .daysOfBooking(2)
            .status(StatusBooking.STATUS_INITIAL)
            .price(1000.00)
            .amount(7000.00)
            .prepaymentAmount(1000.00)
            .isPrepaid(true)
            .room(roomDto)
            .pets(List.of(petDto))
            .build();
    private final Booking newBooking = Booking.builder()
            .price(1000.00)
            .amount(7000.00)
            .prepaymentAmount(1000.00)
            .isPrepaid(true)
            .build();
    private final Booking updatedBooking = Booking.builder()
            .id(bookingId)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(checkIn)
            .checkOutDate(checkOut)
            .status(StatusBooking.STATUS_INITIAL)
            .price(1000.00)
            .amount(7000.00)
            .prepaymentAmount(1000.00)
            .isPrepaid(true)
            .room(room)
            .pets(List.of(pet))
            .build();
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private EntityService entityService;
    @Mock
    private UtilityService utilityService;
    @Mock
    private BookingMapper bookingMapper;

    @Test
    void addBooking_whenAddBookingByBoss_thenBookingAdded() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(entityService.getListOfPetsByIds(any())).thenReturn(List.of(pet));
        when(bookingMapper.toBooking(any(NewBookingDto.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.addBooking(boss.getId(), newBookingDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(bookingDto.getType(), result.getType());
        Assertions.assertEquals(bookingDto.getCheckInDate(), result.getCheckInDate());
        Assertions.assertEquals(bookingDto.getCheckOutDate(), result.getCheckOutDate());
        Assertions.assertEquals(bookingDto.getDaysOfBooking(), result.getDaysOfBooking());
        Assertions.assertEquals(bookingDto.getStatus(), result.getStatus());
        Assertions.assertEquals(bookingDto.getPrice(), result.getPrice());
        Assertions.assertEquals(bookingDto.getAmount(), result.getAmount());
        Assertions.assertEquals(bookingDto.getPrepaymentAmount(), result.getPrepaymentAmount());
        Assertions.assertEquals(bookingDto.getIsPrepaid(), result.getIsPrepaid());
        Assertions.assertEquals(bookingDto.getRoom(), result.getRoom());
        Assertions.assertEquals(bookingDto.getPets(), result.getPets());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void addBooking_whenAddBookingAndRoomNotFound_thenNotFoundException() {
        doThrow(new NotFoundException(String.format("Room with id=%d is not found", user.getId())))
                .when(entityService).getRoomIfExists(anyLong());

        assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(user.getId(), newBookingDto));
    }

    @Test
    void getBookingById_whenGetBookingByBoss_thenReturnedBooking() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        when(entityService.getBookingIfExists(anyLong())).thenReturn(booking);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(boss.getId(), bookingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(bookingDto.getType(), result.getType());
        Assertions.assertEquals(bookingDto.getCheckInDate(), result.getCheckInDate());
        Assertions.assertEquals(bookingDto.getCheckOutDate(), result.getCheckOutDate());
        Assertions.assertEquals(bookingDto.getDaysOfBooking(), result.getDaysOfBooking());
        Assertions.assertEquals(bookingDto.getStatus(), result.getStatus());
        Assertions.assertEquals(bookingDto.getPrice(), result.getPrice());
        Assertions.assertEquals(bookingDto.getAmount(), result.getAmount());
        Assertions.assertEquals(bookingDto.getPrepaymentAmount(), result.getPrepaymentAmount());
        Assertions.assertEquals(bookingDto.getIsPrepaid(), result.getIsPrepaid());
        Assertions.assertEquals(bookingDto.getRoom(), result.getRoom());
        Assertions.assertEquals(bookingDto.getPets(), result.getPets());
    }

    @Test
    void getBookingById_whenGetBookingByIdAndBookingNotFound_thenNotFoundException() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        doThrow(new NotFoundException(String.format("Booking with id=%d is not found", user.getId())))
                .when(entityService).getBookingIfExists(anyLong());

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(user.getId(), bookingId));
    }

    @Test
    void updateBookingById_whenRequesterBossAndBookingFound_thenUpdateAllFields() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        when(entityService.getBookingIfExists(anyLong())).thenReturn(booking);
        when(bookingMapper.toBooking(any(UpdateBookingDto.class))).thenReturn(newBooking);
        when(entityService.getRoomIfExists(anyLong())).thenReturn(room);
        when(entityService.getPetIfExists(anyLong())).thenReturn(pet);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(updatedBookingDto);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        BookingDto result = bookingService.updateBooking(boss.getId(), bookingId, updateBookingDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(updatedBookingDto.getType(), result.getType());
        Assertions.assertEquals(updatedBookingDto.getCheckInDate(), result.getCheckInDate());
        Assertions.assertEquals(updatedBookingDto.getCheckOutDate(), result.getCheckOutDate());
        Assertions.assertEquals(updatedBookingDto.getDaysOfBooking(), result.getDaysOfBooking());
        Assertions.assertEquals(updatedBookingDto.getStatus(), result.getStatus());
        Assertions.assertEquals(updatedBookingDto.getPrice(), result.getPrice());
        Assertions.assertEquals(updatedBookingDto.getAmount(), result.getAmount());
        Assertions.assertEquals(updatedBookingDto.getPrepaymentAmount(), result.getPrepaymentAmount());
        Assertions.assertEquals(updatedBookingDto.getIsPrepaid(), result.getIsPrepaid());
        Assertions.assertEquals(updatedBookingDto.getRoom(), result.getRoom());
        Assertions.assertEquals(updatedBookingDto.getPets(), result.getPets());

        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingById_whenRequesterFoundAndBookingNotFound_thenNotFoundException() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        doThrow(new NotFoundException(String.format("Booking with id=%d is not found", user.getId())))
                .when(entityService).getBookingIfExists(anyLong());

        assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(boss.getId(), bookingId, new UpdateBookingDto()));
    }

    @Test
    void deleteBookingId_whenRequesterBossAndBookingFound_thenBookingDeleted() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        when(bookingRepository.deleteBookingById(anyLong())).thenReturn(1);

        bookingService.deleteBookingById(boss.getId(), bookingId);

        verify(bookingRepository, times(1)).deleteBookingById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void deleteBookingById_whenRequesterNotFound_thenNotFoundException() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> bookingService.deleteBookingById(boss.getId(), bookingId));
    }

    @Test
    void deleteBookingById_whenRequesterFoundAndBookingNotFound_thenNotFoundException() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);
        when(entityService.getBookingIfExists(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> bookingService.deleteBookingById(boss.getId(), bookingId));
    }

    @Test
    void findCrossingBookingsForRoomInDates_whenOneCrossingBooking_thenReturnedListOfBooking() {
        when(bookingRepository.findCrossingBookingsForRoomInDates(anyLong(), any(), any())).thenReturn(Optional.of(List.of(booking)));
        when(bookingMapper.toBookingDto(anyList())).thenReturn(List.of(bookingDto));

        List<BookingDto> result = bookingService.findCrossingBookingsForRoomInDates(boss.getId(), room.getId(), checkIn, checkOut);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(bookingDto.getType(), result.get(0).getType());
        Assertions.assertEquals(bookingDto.getCheckInDate(), result.get(0).getCheckInDate());
        Assertions.assertEquals(bookingDto.getCheckOutDate(), result.get(0).getCheckOutDate());
        Assertions.assertEquals(bookingDto.getDaysOfBooking(), result.get(0).getDaysOfBooking());
        Assertions.assertEquals(bookingDto.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(bookingDto.getPrice(), result.get(0).getPrice());
        Assertions.assertEquals(bookingDto.getAmount(), result.get(0).getAmount());
        Assertions.assertEquals(bookingDto.getPrepaymentAmount(), result.get(0).getPrepaymentAmount());
        Assertions.assertEquals(bookingDto.getIsPrepaid(), result.get(0).getIsPrepaid());
        Assertions.assertEquals(bookingDto.getRoom(), result.get(0).getRoom());
        Assertions.assertEquals(bookingDto.getPets(), result.get(0).getPets());

        verify(bookingRepository, times(1)).findCrossingBookingsForRoomInDates(anyLong(), any(), any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void findBlockingBookingsForRoomInDates_whenOneBlockingBooking_thenReturnedListOfBooking() {
        when(bookingRepository.findBookingsForRoomInDates(anyLong(), any(), any())).thenReturn(Optional.of(List.of(booking)));
        when(bookingMapper.toBookingDto(anyList())).thenReturn(List.of(bookingDto));

        List<BookingDto> result = bookingService.findBlockingBookingsForRoomInDates(boss.getId(), room.getId(), checkIn, checkOut);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(bookingDto.getType(), result.get(0).getType());
        Assertions.assertEquals(bookingDto.getCheckInDate(), result.get(0).getCheckInDate());
        Assertions.assertEquals(bookingDto.getCheckOutDate(), result.get(0).getCheckOutDate());
        Assertions.assertEquals(bookingDto.getDaysOfBooking(), result.get(0).getDaysOfBooking());
        Assertions.assertEquals(bookingDto.getStatus(), result.get(0).getStatus());
        Assertions.assertEquals(bookingDto.getPrice(), result.get(0).getPrice());
        Assertions.assertEquals(bookingDto.getAmount(), result.get(0).getAmount());
        Assertions.assertEquals(bookingDto.getPrepaymentAmount(), result.get(0).getPrepaymentAmount());
        Assertions.assertEquals(bookingDto.getIsPrepaid(), result.get(0).getIsPrepaid());
        Assertions.assertEquals(bookingDto.getRoom(), result.get(0).getRoom());
        Assertions.assertEquals(bookingDto.getPets(), result.get(0).getPets());

        verify(bookingRepository, times(1)).findBookingsForRoomInDates(anyLong(), any(), any());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void checkRoomAvailableInDates_whenOneBlockingBooking_thenConflictException() {
        when(bookingRepository.findBookingsForRoomInDates(anyLong(), any(), any())).thenReturn(Optional.of(List.of(booking)));

        assertThrows(ConflictException.class,
                () -> bookingService.checkRoomAvailableInDates(boss.getId(), room.getId(), checkIn, checkOut));
    }

    @Test
    void checkRoomAvailableInDates_whenNoBlockingBooking_thenNoConflictException() {
        when(bookingRepository.findBookingsForRoomInDates(anyLong(), any(), any())).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> bookingService.checkRoomAvailableInDates(boss.getId(), room.getId(), checkIn, checkOut));
    }
}
