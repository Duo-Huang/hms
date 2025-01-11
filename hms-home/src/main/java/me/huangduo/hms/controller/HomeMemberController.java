package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.dto.request.MemberInfoUpdateRequest;
import me.huangduo.hms.dto.request.MemberInvitationRequest;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import me.huangduo.hms.service.HomeMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/homes")
public class HomeMemberController {

    private final HomeMemberService homeMemberService;

    public HomeMemberController(HomeMemberService homeMemberService) {
        this.homeMemberService = homeMemberService;
    }

    @PostMapping("/{homeId}/members/invite")
    public ResponseEntity<HmsResponse<Void>> inviteMember(@PathVariable Integer homeId, @RequestBody MemberInvitationRequest memberInvitationRequest) {
        User user = new User();
        user.setUsername(memberInvitationRequest.username());
        homeMemberService.inviteMember(homeId, user);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @PostMapping("/{homeId}/members")
    public ResponseEntity<HmsResponse<Void>> acceptInvitation(@PathVariable Integer homeId, @RequestAttribute UserToken userToken) {
        homeMemberService.addMember(homeId, userToken.userInfo().getUserId());
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{homeId}/members/{userId}")
    public ResponseEntity<HmsResponse<Void>> removeMember(@PathVariable Integer homeId, @PathVariable Integer userId) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.removeMember(member);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @PutMapping("{homeId}/members/{userId}")
    public ResponseEntity<HmsResponse<Void>> updateMemberInfo(@PathVariable Integer homeId, @PathVariable Integer userId, @Valid @RequestBody MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        member.setMemberName(memberInfoUpdateRequest.memberName());
        homeMemberService.updateMemberInfo(member);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @GetMapping("")
    public ResponseEntity<HmsResponse<HomeInfoResponse>> getHomesForMember() {
        return null;
    }
}
