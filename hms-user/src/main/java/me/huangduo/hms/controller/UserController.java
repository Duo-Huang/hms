package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.dto.model.User;
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

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<HmsResponse<UserRegistrationResponse>> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        // TODO: use auto mapper ?
        try {
        User user = new User(null, userRegistrationRequest.username(), null, null, null);
        Integer userId = userService.register(user, userRegistrationRequest.password());
        return ResponseEntity.ok(HmsResponse.success(new UserRegistrationResponse(userId)));

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(HmsErrorCodeEnum.USER_ERROR_103.getCode(), HmsErrorCodeEnum.USER_ERROR_103.getMessage()));
        }
    }
}
