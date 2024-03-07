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
import ru.modgy.exception.AccessDeniedException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class UtilityServiceTest {
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
            .id(3L)
            .firstName("user")
            .role(Roles.ROLE_USER)
            .isActive(true)
            .build();
    private final User financial = User.builder()
            .email("financial@pethotel.ru")
            .id(4L)
            .firstName("financial")
            .role(Roles.ROLE_FINANCIAL)
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
    private UtilityService utilityService;
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

        User result = utilityService.getUserIfExists(user.getId());

        Assertions.assertEquals(result, user);
        verify(userRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserIfExists_whenUserNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> utilityService.getUserIfExists(user.getId()));
    }

    @Test
    void getCategoryIfExists_whenCategoryFound_thenReturnedCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category result = utilityService.getCategoryIfExists(category.getId());

        Assertions.assertEquals(result, category);
        verify(categoryRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoryIfExists_whenCategoryNotFound_thenNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> utilityService.getCategoryIfExists(category.getId()));
    }

    @Test
    void getRoomIfExists_whenRoomFound_thenReturnedRoom() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        Room result = utilityService.getRoomIfExists(room.getId());

        Assertions.assertEquals(result, room);
        verify(roomRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomIfExists_whenRoomNotFound_thenNotFoundException() {
        when(roomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> utilityService.getRoomIfExists(room.getId()));
    }

    @Test
    void getPetIfExists_whenPetFound_thenReturnedPet() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));

        Pet result = utilityService.getPetIfExists(pet.getId());

        Assertions.assertEquals(result, pet);
        verify(petRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(petRepository);
    }

    @Test
    void getPetIfExists_whenPetNotFound_thenNotFoundException() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> utilityService.getPetIfExists(pet.getId()));
    }

    @Test
    void getBookingIfExists_whenBookingFound_thenReturnedBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking result = utilityService.getBookingIfExists(booking.getId());

        Assertions.assertEquals(result, booking);
        verify(bookingRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingIfExists_whenBookingNotFound_thenNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> utilityService.getBookingIfExists(booking.getId()));
    }

    @Test
    void getListOfPetsByIds_whenPetsFound_thenReturnedListOfPets() {
        when(petRepository.findAllByIdIn(anyList())).thenReturn(Optional.of(List.of(pet)));

        List<Pet> result = utilityService.getListOfPetsByIds(List.of(pet.getId()));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(result.get(0), pet);
    }

    @Test
    void getListOfPetsByIds_whenPetsNotFound_thenConflictException() {
        assertThrows(ConflictException.class,
                () -> utilityService.getListOfPetsByIds(List.of(pet.getId())));
    }

    @Test
    void checkBossAdminAccess_whenCheckBoss_thenAccessGranted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminAccess(boss.getId()));
    }

    @Test
    void checkBossAdminAccess_whenCheckAdmin_thenAccessGranted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminAccess(admin.getId()));
    }

    @Test
    void checkBossAdminAccess_whenCheckUser_thenAccessDenied() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> utilityService.checkBossAdminAccess(user.getId()));
    }

    @Test
    void checkBossAdminAccess_whenCheckFinancial_thenAccessDenied() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class, () -> utilityService.checkBossAdminAccess(user.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckBoss_thenAccessGranted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminFinancialAccess(boss.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckAdmin_thenAccessGranted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminFinancialAccess(admin.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckUser_thenAccessDenied() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> utilityService.checkBossAdminAccess(user.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckFinancial_thenAccessGranted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminFinancialAccess(financial.getId()));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(boss, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndAdmin_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccess(boss, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccess(boss, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndFinancial_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccess(boss, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(admin, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(admin, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccess(admin, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndFinancial_thenAccessDenied() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccess(admin, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndBoss_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(boss, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndAdmin_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(boss, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(boss, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndFinancial_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(boss, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(admin, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndAdmin_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(admin, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(admin, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndFinancial_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccess(admin, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(user, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccess(financial, Roles.ROLE_FINANCIAL));
    }
}
