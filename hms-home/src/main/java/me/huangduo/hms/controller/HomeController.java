package me.huangduo.hms.controller;


import jakarta.validation.Valid;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.service.HomeService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/homes")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @PostMapping
    public ResponseEntity<HmsResponse<Void>> createHome(@Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) {
        try {
            homeService.createHome(homeCreateOrUpdateRequest);
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(HmsErrorCodeEnum.HOME_ERROR_201.getCode(), HmsErrorCodeEnum.HOME_ERROR_201.getMessage()));
        }
    }

    @GetMapping("/{homeId}")
    public ResponseEntity<HmsResponse<HomeInfoResponse>> getHomeInfo(@PathVariable Integer homeId) {
        HomeEntity homeInfo = homeService.getHomeInfo(homeId);
        return ResponseEntity.ok(HmsResponse.success(new HomeInfoResponse(homeInfo.getHomeId(), homeInfo.getHomeName(), homeInfo.getHomeDescription())));
    }

    @PutMapping("/{homeId}") // 测试不传id的路由
    public ResponseEntity<HmsResponse<Void>> updateHomeInfo(@PathVariable Integer homeId, @Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) {
        homeService.updateHomeInfo(homeId, homeCreateOrUpdateRequest);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{homeId}")
    public ResponseEntity<HmsResponse<Void>> deleteHome(@PathVariable Integer homeId) {
        homeService.deleteHome(homeId);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
