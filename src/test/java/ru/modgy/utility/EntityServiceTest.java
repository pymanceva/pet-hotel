package ru.modgy.utility;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;
import ru.modgy.pet.repository.PetRepository;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.category.repository.CategoryRepository;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class EntityServiceTest {
    private final User user = User.builder()
            .email("user@pethotel.ru")
            .id(3L)
            .firstName("user")
            .role(Roles.ROLE_USER)
            .isActive(true)
            .build();
    private final Category category = Category.builder()
            .id(1L)
            .name("Dog room")
            .description("Room for dogs")
            .build();
    private final Room room = Room.builder()
            .id(1L)
            .area(5.0)
            .number("standard room")
            .category(category)
            .isVisible(true)
            .build();
    private final Pet pet = Pet.builder()
            .id(1L)
            .type(TypeOfPet.DOG)
            .name("Шарик")
            .breed("Spaniel")
            .birthDate(LocalDate.of(2023, 1, 1))
            .sex(Sex.FEMALE)
            .build();
    private final Booking booking = Booking.builder()
            .id(1L)
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
    @InjectMocks
    private EntityService entityService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Test
    void getUserIfExists_whenUserFound_thenReturnedUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = entityService.getUserIfExists(user.getId());

        Assertions.assertEquals(result, user);
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserIfExists_whenUserNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> entityService.getUserIfExists(user.getId()));
    }

    @Test
    void getCategoryIfExists_whenCategoryFound_thenReturnedCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category result = entityService.getCategoryIfExists(category.getId());

        Assertions.assertEquals(result, category);
        verify(categoryRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoryIfExists_whenCategoryNotFound_thenNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> entityService.getCategoryIfExists(category.getId()));
    }

    @Test
    void getRoomIfExists_whenRoomFound_thenReturnedRoom() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        Room result = entityService.getRoomIfExists(room.getId());

        Assertions.assertEquals(result, room);
        verify(roomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomIfExists_whenRoomNotFound_thenNotFoundException() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> entityService.getRoomIfExists(room.getId()));
    }

    @Test
    void getPetIfExists_whenPetFound_thenReturnedPet() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));

        Pet result = entityService.getPetIfExists(pet.getId());

        Assertions.assertEquals(result, pet);
        verify(petRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(petRepository);
    }

    @Test
    void getPetIfExists_whenPetNotFound_thenNotFoundException() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> entityService.getPetIfExists(pet.getId()));
    }

    @Test
    void getBookingIfExists_whenBookingFound_thenReturnedBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking result = entityService.getBookingIfExists(booking.getId());

        Assertions.assertEquals(result, booking);
        verify(bookingRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingIfExists_whenBookingNotFound_thenNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> entityService.getBookingIfExists(booking.getId()));
    }

    @Test
    void getListOfPetsByIds_whenPetsFound_thenReturnedListOfPets() {
        when(petRepository.findAllByIdIn(anyList())).thenReturn(Optional.of(List.of(pet)));

        List<Pet> result = entityService.getListOfPetsByIds(List.of(pet.getId()));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(result.get(0), pet);
    }

    @Test
    void getListOfPetsByIds_whenPetsNotFound_thenConflictException() {
        assertThrows(ConflictException.class,
                () -> entityService.getListOfPetsByIds(List.of(pet.getId())));
    }
}
