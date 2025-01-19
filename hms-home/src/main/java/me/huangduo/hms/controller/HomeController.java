package me.huangduo.hms.controller;


import jakarta.validation.Valid;
import me.huangduo.hms.annotations.ValidId;
import me.huangduo.hms.dto.response.HmsResponse;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
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
    public ResponseEntity<HmsResponse<Void>> createHome(@Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest, @RequestAttribute UserToken userToken) {
        try {
            homeService.createHome(homeMapper.toModel(homeCreateOrUpdateRequest), userToken.userInfo());
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum()));
        }
    }

    @GetMapping("/{homeId:\\d+}")
    public ResponseEntity<HmsResponse<HomeInfoResponse>> getHomeInfo(@ValidId @PathVariable Integer homeId) {
        Home homeInfo = homeService.getHomeInfo(homeId);
        return ResponseEntity.ok(HmsResponse.success(homeMapper.toResponse(homeInfo)));
    }

    @PatchMapping("/{homeId:\\d+}")
    public ResponseEntity<HmsResponse<Void>> updateHomeInfo(@ValidId @PathVariable Integer homeId, @Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) {
        Home home = homeMapper.toModel(homeCreateOrUpdateRequest);
        home.setHomeId(homeId);
        homeService.updateHomeInfo(home);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{homeId:\\d+}")
    public ResponseEntity<HmsResponse<Void>> deleteHome(@ValidId @PathVariable Integer homeId) {
        homeService.deleteHome(homeId);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
