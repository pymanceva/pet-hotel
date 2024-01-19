package ru.dogudacha.PetHotel.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.room.category.model.Category;
import ru.dogudacha.PetHotel.room.category.repository.CategoryRepository;
import ru.dogudacha.PetHotel.room.model.Room;
import ru.dogudacha.PetHotel.room.repository.RoomRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UtilityService {
    public static final String REQUESTER_ID_HEADER = "X-PetHotel-User-Id";
    final private UserRepository userRepository;
    final private CategoryRepository categoryRepository;
    final private RoomRepository roomRepository;
    final private PetRepository petRepository;

    public User getUserIfExists(long userId) {
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
                new NotFoundException(String.format("Pet with id = %d not found", petId)));
    }

    /* Раскомментить после мержа бронирований
    public static Booking getBookingIfExists(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Booking with id=%d is not found", id)));
    }
    */

    //Проверка пользователя на роль BOSS или ADMIN, иначе AccessDeniedException
    public void checkBossAdminAccess(Long userId) {
        User user = getUserIfExists(userId);

        if (user.getRole().ordinal() >= 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
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

        if (user.getRole().ordinal() == 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
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

        if (requester.getRole().ordinal() < 2 &&
                (role == null ||
                        (requester.getRole().ordinal() < role.ordinal()))
        ) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                requester.getRole()));
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

        if (requester.getRole().ordinal() < 2 &&
                requester.getRole().ordinal() <= role.ordinal()) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                requester.getRole()));
    }
}
