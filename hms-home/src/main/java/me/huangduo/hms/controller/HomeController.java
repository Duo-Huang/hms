package me.huangduo.hms.controller;


import jakarta.validation.Valid;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.service.HomeService;
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
    public ResponseEntity<HmsResponse<Void>> createHome(@Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest, @RequestAttribute UserToken userToken) {
        Home home = new Home(null, homeCreateOrUpdateRequest.homeName(), homeCreateOrUpdateRequest.homeDescription(), null, null);
        try {
            homeService.createHome(home, userToken.userInfo());
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum().getCode(), e.getHmsErrorCodeEnum().getMessage()));
        }
    }

    @GetMapping("/{homeId}")
    public ResponseEntity<HmsResponse<HomeInfoResponse>> getHomeInfo(@PathVariable Integer homeId) {
        Home homeInfo = homeService.getHomeInfo(homeId);
        return ResponseEntity.ok(HmsResponse.success(new HomeInfoResponse(homeInfo.getHomeId(), homeInfo.getHomeName(), homeInfo.getHomeDescription(), homeInfo.getCreatedAt())));
    }

    @PutMapping("/{homeId}")
    public ResponseEntity<HmsResponse<Void>> updateHomeInfo(@PathVariable Integer homeId, @Valid @RequestBody HomeCreateOrUpdateRequest homeCreateOrUpdateRequest) {
        Home home = new Home(null, homeCreateOrUpdateRequest.homeName(), homeCreateOrUpdateRequest.homeDescription(), null, null);
        homeService.updateHomeInfo(home);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{homeId}")
    public ResponseEntity<HmsResponse<Void>> deleteHome(@PathVariable Integer homeId) {
        homeService.deleteHome(homeId);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
