package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.dto.request.UserLoginRequest;
import me.huangduo.hms.dto.request.UserPasswordUpdateRequest;
import me.huangduo.hms.dto.request.UserProfileUpdateRequest;
import me.huangduo.hms.dto.request.UserRegistrationRequest;
import me.huangduo.hms.dto.response.UserRegistrationResponse;
import me.huangduo.hms.enums.ErrorCode;
import me.huangduo.hms.exceptions.DuplicatedPasswordException;
import me.huangduo.hms.exceptions.UserAlreadyExistsException;
import me.huangduo.hms.mapper.UserMapper;
import me.huangduo.hms.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<HmsResponseBody<UserRegistrationResponse>> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        try {
            Integer userId = userService.register(userMapper.toModel(userRegistrationRequest), userRegistrationRequest.password());
            return ResponseEntity.ok(HmsResponseBody.success(new UserRegistrationResponse(userId)));

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponseBody.error(e.getErrorCode()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<HmsResponseBody<String>> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        try {
            String token = userService.login(userMapper.toModel(userLoginRequest), userLoginRequest.password());
            return ResponseEntity.ok(HmsResponseBody.success(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(HmsResponseBody.error(ErrorCode.USER_ERROR_105));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<HmsResponseBody<Void>> logout(@RequestAttribute UserToken userToken) {
        userService.logout(userToken);
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @PutMapping("/password")
    public ResponseEntity<HmsResponseBody<Void>> changePassword(
            @Valid @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest,
            @RequestAttribute UserToken userToken
    ) {
        try {
            userService.changePassword(userToken, userPasswordUpdateRequest.oldPassword(), userPasswordUpdateRequest.newPassword());
            return ResponseEntity.ok(HmsResponseBody.success());
        } catch (DuplicatedPasswordException e) {
            return ResponseEntity.badRequest().body(HmsResponseBody.error(e.getErrorCode()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(HmsResponseBody.error(ErrorCode.USER_ERROR_108));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<HmsResponseBody<User>> getProfile(@RequestAttribute UserToken userToken) {
        User profile = userService.getProfile(userToken.userInfo().getUserId());
        return ResponseEntity.ok(HmsResponseBody.success(profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<HmsResponseBody<Void>> getProfile(
            @Valid @RequestBody UserProfileUpdateRequest userProfileUpdateRequest,
            @RequestAttribute UserToken userToken
    ) {
        User profile = userToken.userInfo();
        profile.setNickname(userProfileUpdateRequest.nickname());
        userService.updateProfile(profile);
        return ResponseEntity.ok(HmsResponseBody.success());
    }
}
