package me.huangduo.hms.aspect;

import jakarta.servlet.http.HttpServletRequest;
import me.huangduo.hms.annotations.PermissionCheck;
import me.huangduo.hms.enums.ErrorCodeEnum;
import me.huangduo.hms.exceptions.AccessDeniedException;
import me.huangduo.hms.model.HomeRole;
import me.huangduo.hms.model.User;
import me.huangduo.hms.service.HomeRoleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class PermissionCheckAspect {

    private final HomeRoleService homeRoleService;

    public PermissionCheckAspect(HomeRoleService homeRoleService) {
        this.homeRoleService = homeRoleService;
    }

    @Around("@annotation(permissionCheck)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, PermissionCheck permissionCheck) throws Throwable {
        String[] requiredPermissions = permissionCheck.value();
        if (!hasPermission(requiredPermissions)) {
            throw new AccessDeniedException(ErrorCodeEnum.HOME_ERROR_219);
        }
        return joinPoint.proceed();
    }

    private boolean hasPermission(String[] requiredPermissions) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        User user = (User) request.getAttribute("userInfo");

        Integer homeId = (Integer) request.getAttribute("homeId");
        HomeRole homeRole = homeRoleService.getHomeRoleWithPermissions(homeId, user.getUserId());

        return Arrays.stream(requiredPermissions).allMatch(permissionCode -> homeRole.getPermissions().stream().anyMatch(permission -> permission.getPermissionCode().equals(permissionCode)));
    }
}
