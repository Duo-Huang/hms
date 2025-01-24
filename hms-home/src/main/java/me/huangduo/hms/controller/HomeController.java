package me.huangduo.hms.controller;


import jakarta.validation.Valid;
import me.huangduo.hms.annotations.ValidId;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.mapper.HomeMapper;
import me.huangduo.hms.service.HomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/homes")
@Validated
public class HomeController {

    private final HomeService homeService;

    private final HomeMapper homeMapper;

    public HomeController(HomeService homeService, HomeMapper homeMapper) {
        this.homeService = homeService;
        this.homeMapper = homeMapper;
    }

    @PostMapping
    public ResponseEntity<HmsResponseBody<Void>> createHome(@Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest, @RequestAttribute User userInfo) {
        try {
            homeService.createHome(homeMapper.toModel(homeCreateOrUpdateRequest), userInfo);
            return ResponseEntity.ok(HmsResponseBody.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponseBody.error(e.getErrorCode()));
        }
    }

    @GetMapping("/{homeId:\\d+}")
    public ResponseEntity<HmsResponseBody<HomeInfoResponse>> getHomeInfo(@ValidId @PathVariable Integer homeId) {
        Home homeInfo = homeService.getHomeInfo(homeId);
        return ResponseEntity.ok(HmsResponseBody.success(homeMapper.toResponse(homeInfo)));
    }

    @PatchMapping("/{homeId:\\d+}")
    public ResponseEntity<HmsResponseBody<Void>> updateHomeInfo(@ValidId @PathVariable Integer homeId, @Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) {
        Home home = homeMapper.toModel(homeCreateOrUpdateRequest);
        home.setHomeId(homeId);
        homeService.updateHomeInfo(home);
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @DeleteMapping("/{homeId:\\d+}")
    public ResponseEntity<HmsResponseBody<Void>> deleteHome(@ValidId @PathVariable Integer homeId) {
        homeService.deleteHome(homeId);
        return ResponseEntity.ok(HmsResponseBody.success());
    }
}
