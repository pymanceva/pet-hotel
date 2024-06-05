package ru.modgy.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.ConflictException;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UtilityService {
    public static final String REQUESTER_ID_HEADER = "X-PetHotel-User-Id";
    private final EntityService entityService;

    public void checkBossAdminAccess(User user) {
        if (user.getRole().ordinal() >= 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
    }

    public void checkBossAdminAccess(Long userId) {
        User user = entityService.getUserIfExists(userId);
        checkBossAdminAccess(user);
    }

    public void checkBossAdminFinancialAccess(User user) {
        if (user.getRole().ordinal() == 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
    }

    public void checkBossAdminFinancialAccess(Long userId) {
        User user = entityService.getUserIfExists(userId);
        checkBossAdminFinancialAccess(user);
    }

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
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccess(requester, user.getRole());
    }

    public void checkHigherOrdinalRoleAccess(Long requesterId, Roles role) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccess(requester, role);
    }

    public void checkHigherOrdinalRoleAccess(Long requesterId, Long userId) {
        User requester = entityService.getUserIfExists(requesterId);
        User user = entityService.getUserIfExists(userId);
        checkHigherOrdinalRoleAccess(requester, user);
    }

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
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrEqualOrdinalRoleAccess(requester, user.getRole());
    }

    public void checkHigherOrEqualOrdinalRoleAccess(Long requesterId, Roles role) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrEqualOrdinalRoleAccess(requester, role);
    }

    public void checkHigherOrEqualOrdinalRoleAccess(Long requesterId, Long userId) {
        User requester = entityService.getUserIfExists(requesterId);
        User user = entityService.getUserIfExists(userId);
        checkHigherOrEqualOrdinalRoleAccess(requester, user);
    }

    public boolean checkRequesterRequestsHimself(Long requesterId, Long userId) {
        return userId.equals(requesterId);
    }

    public void checkDatesOfBooking(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new ConflictException(String.format("CheckInDate=%s is after CheckOutDate=%s",
                    checkInDate, checkOutDate));
        }
    }
}
