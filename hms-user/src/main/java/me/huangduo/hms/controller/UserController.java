package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.dto.request.UserLoginRequest;
import me.huangduo.hms.dto.request.UserRegistrationRequest;
import me.huangduo.hms.dto.response.UserRegistrationResponse;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import me.huangduo.hms.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<HmsResponse<UserRegistrationResponse>> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        try {
            // TODO: use auto mapper ?
            User user = new User(null, userRegistrationRequest.username(), null, null, null);
            Integer userId = userService.register(user, userRegistrationRequest.password());
            return ResponseEntity.ok(HmsResponse.success(new UserRegistrationResponse(userId)));

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(HmsErrorCodeEnum.USER_ERROR_103.getCode(), HmsErrorCodeEnum.USER_ERROR_103.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<HmsResponse<String>> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        try {
            // TODO: use auto mapper ?
            User user = new User(null, userLoginRequest.username(), null, null, null);
            String token = userService.login(user, userLoginRequest.password());
            return ResponseEntity.ok(HmsResponse.success(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.USER_ERROR_106.getCode(), HmsErrorCodeEnum.USER_ERROR_106.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<HmsResponse<?>> logout(@RequestAttribute UserToken userToken) {
        userService.logout(userToken);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
