package ru.modgy.utility;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private UtilityService utilityService;
    @Mock
    private EntityService entityService;

    @Test
    void checkBossAdminAccess_whenCheckBoss_thenAccessGranted() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminAccess(boss.getId()));
    }

    @Test
    void checkBossAdminAccess_whenCheckAdmin_thenAccessGranted() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(admin);

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminAccess(admin.getId()));
    }

    @Test
    void checkBossAdminAccess_whenCheckUser_thenAccessDenied() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(AccessDeniedException.class, () -> utilityService.checkBossAdminAccess(user.getId()));
    }

    @Test
    void checkBossAdminAccess_whenCheckFinancial_thenAccessDenied() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(financial);

        assertThrows(AccessDeniedException.class, () -> utilityService.checkBossAdminAccess(user.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckBoss_thenAccessGranted() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(boss);

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminFinancialAccess(boss.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckAdmin_thenAccessGranted() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(admin);

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminFinancialAccess(admin.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckUser_thenAccessDenied() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(AccessDeniedException.class, () -> utilityService.checkBossAdminAccess(user.getId()));
    }

    @Test
    void checkBossAdminFinancialAccess_whenCheckFinancial_thenAccessGranted() {
        when(entityService.getUserIfExists(anyLong())).thenReturn(financial);

        Assertions.assertDoesNotThrow(() -> utilityService.checkBossAdminFinancialAccess(financial.getId()));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(boss, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndAdmin_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccessForUsers(boss, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccessForUsers(boss, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckBossAndFinancial_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccessForUsers(boss, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(admin, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(admin, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccessForUsers(admin, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckAdminAndFinancial_thenAccessDenied() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrdinalRoleAccessForUsers(admin, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckUserAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrdinalRoleAccess_whenCheckFinancialAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndBoss_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(boss, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndAdmin_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(boss, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(boss, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckBossAndFinancial_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(boss, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(admin, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndAdmin_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(admin, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndUser_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(admin, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckAdminAndFinancial_thenAccessGranted() {
        Assertions.assertDoesNotThrow(() -> utilityService.checkHigherOrEqualOrdinalRoleAccessForUsers(admin, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckUserAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(user, Roles.ROLE_FINANCIAL));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndBoss_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_BOSS));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndAdmin_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_ADMIN));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndUser_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_USER));
    }

    @Test
    void checkHigherOrEqualOrdinalRoleAccess_whenCheckFinancialAndFinancial_thenAccessDenied() {
        assertThrows(AccessDeniedException.class, () -> utilityService.checkHigherOrdinalRoleAccessForUsers(financial, Roles.ROLE_FINANCIAL));
    }
}
