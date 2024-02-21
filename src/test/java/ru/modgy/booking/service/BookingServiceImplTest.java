package ru.modgy.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.dto.mapper.BookingMapper;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;
import ru.modgy.pet.repository.PetRepository;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingServiceImplTest {
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
    private final User admin = User.builder()
            .email("admin@pethotel.ru")
            .id(2L)
            .firstName("admin")
            .role(Roles.ROLE_ADMIN)
            .isActive(true)
            .build();
    private final User user = User.builder()
            .email("user@pethotel.ru")
            .id(2L)
            .firstName("user")
            .role(Roles.ROLE_USER)
            .isActive(true)
            .build();
    private final User financial = User.builder()
            .email("financial@pethotel.ru")
            .id(2L)
            .firstName("financial")
            .role(Roles.ROLE_FINANCIAL)
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
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .petIds(List.of(1L))
            .build();
    private final Long bookingId = 1L;
    private final Booking booking = Booking.builder()
            .id(bookingId)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
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
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
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
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .daysOfBooking(7)
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
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
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
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private BookingMapper bookingMapper;

    @Test
    void addBooking_whenAddBookingByBoss_thenBookingAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(petRepository.findAllByIdIn(any())).thenReturn(Optional.of(List.of(pet)));
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
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void addBooking_whenAddBookingByAdmin_thenBookingAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(petRepository.findAllByIdIn(any())).thenReturn(Optional.of(List.of(pet)));
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
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void addBooking_whenAddBookingByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.addBooking(user.getId(), newBookingDto));
    }

    @Test
    void addBooking_whenAddBookingByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.addBooking(user.getId(), newBookingDto));
    }

    @Test
    void addBooking_whenAddBookingAndRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(user.getId(), newBookingDto));
    }

    @Test
    void addBooking_whenAddBookingAndRoomNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.addBooking(user.getId(), newBookingDto));
    }

    @Test
    void getBookingById_whenGetBookingByBoss_thenReturnedBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
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

        verify(bookingRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingById_whenGetBookingByAdmin_thenReturnedBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(admin.getId(), bookingId);

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

        verify(bookingRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingById_whenGetBookingByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getBookingById(user.getId(), bookingId));
    }

    @Test
    void getBookingById_whenGetBookingByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getBookingById(financial.getId(), bookingId));
    }

    @Test
    void getBookingById_whenGetBookingByIdAndRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(user.getId(), bookingId));
    }

    @Test
    void getBookingById_whenGetBookingByIdAndBookingNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(user.getId(), bookingId));
    }

    @Test
    void updateBookingById_whenRequesterBossAndBookingFound_thenUpdateAllFields() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBooking(any(UpdateBookingDto.class))).thenReturn(newBooking);
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
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

        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingById_whenRequesterAdminAndBookingFound_thenUpdateAllFields() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBooking(any(UpdateBookingDto.class))).thenReturn(newBooking);
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
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

        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void updateBookingById_whenRequesterUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.updateBooking(user.getId(), bookingId, new UpdateBookingDto()));
    }

    @Test
    void updateBookingById_whenRequesterFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.updateBooking(financial.getId(), bookingId, new UpdateBookingDto()));
    }

    @Test
    void updateBookingById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(boss.getId(), bookingId, new UpdateBookingDto()));
    }

    @Test
    void updateBookingById_whenRequesterFoundAndBookingNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(boss.getId(), bookingId, new UpdateBookingDto()));
    }

    @Test
    void deleteBookingId_whenRequesterBossAndBookingFound_thenBookingDeleted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.deleteBookingById(anyLong())).thenReturn(1);

        bookingService.deleteBookingById(boss.getId(), bookingId);

        verify(bookingRepository, times(1)).deleteBookingById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void deleteBookingId_whenRequesterAdminAndBookingFound_thenBookingDeleted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(bookingRepository.deleteBookingById(anyLong())).thenReturn(1);

        bookingService.deleteBookingById(boss.getId(), bookingId);

        verify(bookingRepository, times(1)).deleteBookingById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void deleteBookingId_whenRequesterUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.deleteBookingById(user.getId(), bookingId));
    }

    @Test
    void deleteBookingId_whenRequesterFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.deleteBookingById(financial.getId(), bookingId));
    }

    @Test
    void deleteBookingById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.deleteBookingById(boss.getId(), bookingId));
    }

    @Test
    void deleteBookingById_whenRequesterFoundAndBookingNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.deleteBookingById(boss.getId(), bookingId));
    }
}
