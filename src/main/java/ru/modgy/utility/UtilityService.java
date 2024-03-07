package ru.modgy.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.repository.PetRepository;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.category.repository.CategoryRepository;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UtilityService {
    public static final String REQUESTER_ID_HEADER = "X-PetHotel-User-Id";
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;
    private final PetRepository petRepository;
    private final BookingRepository bookingRepository;

    public User getUserIfExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d is not found", userId)));
    }

    public Category getCategoryIfExists(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d is not found", id)));
    }

    public Room getRoomIfExists(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Room with id=%d is not found", id)));
    }

    public Pet getPetIfExists(Long petId) {
        return petRepository.findById(petId).orElseThrow(() ->
                new NotFoundException(String.format("Pet with id=%d is not found", petId)));
    }

    public Booking getBookingIfExists(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Booking with id=%d is not found", bookingId)));
    }

    public List<Pet> getListOfPetsByIds(List<Long> petIds) {
        return petRepository.findAllByIdIn(petIds)
                .orElseThrow(() -> new ConflictException("At least one id should be in list"));
    }

    //Проверка пользователя на роль BOSS или ADMIN, иначе AccessDeniedException
    public void checkBossAdminAccess(Long userId) {
        User user = getUserIfExists(userId);
        checkBossAdminAccess(user);
    }

    public void checkBossAdminAccess(User user) {
        if (user.getRole().ordinal() >= 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
    }

    //Проверка пользователя на роль BOSS или ADMIN или FINANCIAL, иначе AccessDeniedException
    public void checkBossAdminFinancialAccess(Long userId) {
        User user = getUserIfExists(userId);
        checkBossAdminFinancialAccess(user);
    }

    public void checkBossAdminFinancialAccess(User user) {
        if (user.getRole().ordinal() == 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
    }

    /*Проверка пользователя на роль BOSS или ADMIN и сравнение со второй ролью (ожидается,
    что пользователь по роли выше второй роли).
    Если роль не соответствует заявленным, либо если вторая роль выше или равна роли проверяемого пользователя
    выбрасывается AccessDeniedException
     */
    public void checkHigherOrdinalRoleAccess(User requester, Roles role) {
        if (requester.getRole().ordinal() < 2 &&
                (role == null ||
                        (requester.getRole().ordinal() < role.ordinal()))
        ) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                requester.getRole()));
    }

    public void checkHigherOrdinalRoleAccess(User requester, User user) {
        checkHigherOrdinalRoleAccess(requester, user.getRole());
    }

    public void checkHigherOrdinalRoleAccess(Long requesterId, User user) {
        User requester = getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccess(requester, user.getRole());
    }

    public void checkHigherOrdinalRoleAccess(Long requesterId, Roles role) {
        User requester = getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccess(requester, role);
    }

    /*Проверка пользователя на роль BOSS или ADMIN и сравнение со второй ролью (ожидается,
    что пользователь по роли выше или равен второй роли).
    Если роль не соответствует заявленным, либо если вторая роль выше роли проверяемого пользователя,
    выбрасывается AccessDeniedException
     */
    public void checkHigherOrEqualOrdinalRoleAccess(User requester, Roles role) {
        if (requester.getRole().ordinal() < 2 &&
                requester.getRole().ordinal() <= role.ordinal()) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                requester.getRole()));
    }

    public void checkHigherOrEqualOrdinalRoleAccess(User requester, User user) {
        checkHigherOrEqualOrdinalRoleAccess(requester, user.getRole());
    }

    public void checkHigherOrEqualOrdinalRoleAccess(Long requesterId, User user) {
        User requester = getUserIfExists(requesterId);
        checkHigherOrEqualOrdinalRoleAccess(requester, user.getRole());
    }

    public void checkHigherOrEqualOrdinalRoleAccess(Long requesterId, Roles role) {
        User requester = getUserIfExists(requesterId);
        checkHigherOrEqualOrdinalRoleAccess(requester, role);
    }
}
