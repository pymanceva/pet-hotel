package ru.modgy.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

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
        if (requester.getRole().ordinal() > role.ordinal()) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    requester.getRole()));
        }
    }

    public void checkHigherOrdinalRoleAccess(Long requesterId, Roles role) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccess(requester, role);
    }

    public void checkHigherOrdinalRoleAccessForUsers(User requester, Roles role) {
        if (requester.getRole().ordinal() < 2 &&
            (role == null ||
             (requester.getRole().ordinal() < role.ordinal()))
        ) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                requester.getRole()));
    }

    public void checkHigherOrdinalRoleAccessForUsers(User requester, User user) {
        checkHigherOrdinalRoleAccessForUsers(requester, user.getRole());
    }

    public void checkHigherOrdinalRoleAccessForUsers(Long requesterId, User user) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccessForUsers(requester, user.getRole());
    }

    public void checkHigherOrdinalRoleAccessForUsers(Long requesterId, Roles role) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrdinalRoleAccessForUsers(requester, role);
    }

    public void checkHigherOrdinalRoleAccessForUsers(Long requesterId, Long userId) {
        User requester = entityService.getUserIfExists(requesterId);
        User user = entityService.getUserIfExists(userId);
        checkHigherOrdinalRoleAccessForUsers(requester, user);
    }

    public void checkHigherOrEqualOrdinalRoleAccessForUsers(User requester, Roles role) {
        if (requester.getRole().ordinal() < 2 &&
            requester.getRole().ordinal() <= role.ordinal()) {
            return;
        }
        throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                requester.getRole()));
    }

    public void checkHigherOrEqualOrdinalRoleAccessForUsers(User requester, User user) {
        checkHigherOrEqualOrdinalRoleAccessForUsers(requester, user.getRole());
    }

    public void checkHigherOrEqualOrdinalRoleAccessForUsers(Long requesterId, User user) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrEqualOrdinalRoleAccessForUsers(requester, user.getRole());
    }

    public void checkHigherOrEqualOrdinalRoleAccessForUsers(Long requesterId, Roles role) {
        User requester = entityService.getUserIfExists(requesterId);
        checkHigherOrEqualOrdinalRoleAccessForUsers(requester, role);
    }

    public void checkHigherOrEqualOrdinalRoleAccessForUsers(Long requesterId, Long userId) {
        User requester = entityService.getUserIfExists(requesterId);
        User user = entityService.getUserIfExists(userId);
        checkHigherOrEqualOrdinalRoleAccessForUsers(requester, user);
    }

    public boolean checkRequesterRequestsHimself(Long requesterId, Long userId) {
        return userId.equals(requesterId);
    }
}
