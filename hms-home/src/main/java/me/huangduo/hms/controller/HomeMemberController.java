package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.dto.request.MemberInfoUpdateRequest;
import me.huangduo.hms.dto.request.MemberInvitationRequest;
import me.huangduo.hms.dto.response.HmsResponse;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import me.huangduo.hms.dto.response.MemberResponse;
import me.huangduo.hms.dto.response.MemberRoleRequest;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.mapper.HomeMapper;
import me.huangduo.hms.mapper.MemberMapper;
import me.huangduo.hms.service.HomeMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/homes")
public class HomeMemberController {

    private final HomeMemberService homeMemberService;

    private final MemberMapper memberMapper;

    private final HomeMapper homeMapper;

    public HomeMemberController(HomeMemberService homeMemberService, MemberMapper memberMapper, HomeMapper homeMapper) {
        this.homeMemberService = homeMemberService;
        this.memberMapper = memberMapper;
        this.homeMapper = homeMapper;
    }

    @PostMapping("/{homeId}/members/invite")
    public ResponseEntity<HmsResponse<Void>> inviteMember(@PathVariable Integer homeId, @Valid @RequestBody MemberInvitationRequest memberInvitationRequest) {
        User user = new User();
        user.setUsername(memberInvitationRequest.username());
        try {
            homeMemberService.inviteMember(homeId, user);
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum().getCode(), e.getHmsErrorCodeEnum().getMessage()));
        }
    }

    @PostMapping("/{homeId}/members")
    public ResponseEntity<HmsResponse<Void>> acceptInvitation(@PathVariable Integer homeId, @RequestAttribute UserToken userToken) {
        try {
            homeMemberService.addMember(homeId, userToken.userInfo());
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum().getCode(), e.getHmsErrorCodeEnum().getMessage()));
        }
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
        Member member = memberMapper.toModel(memberInfoUpdateRequest);
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.updateMemberInfo(member);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @GetMapping("/members/{userId}")
    public ResponseEntity<HmsResponse<List<HomeInfoResponse>>> getHomesForMember(@PathVariable Integer userId) {
        List<Home> homes = homeMemberService.getHomesForUser(Member.builder().userId(userId).build());
        return ResponseEntity.ok(HmsResponse.success(homes.stream().map(homeMapper::toResponse).collect(Collectors.toList())));
    }

    @GetMapping("/{homeId}/members")
    public ResponseEntity<HmsResponse<List<MemberResponse>>> getMembersForHome(@PathVariable Integer homeId) {
        List<Member> members = homeMemberService.getMembersWithRolesForHome(homeId);

        return ResponseEntity.ok(HmsResponse.success(members.stream().map(memberMapper::toResponse).collect(Collectors.toList())));
    }

    @PutMapping("/{homeId}/members/{userId}/role")
    public ResponseEntity<HmsResponse<Void>> assignRoleForMember(@PathVariable Integer homeId, @PathVariable Integer userId, @Valid @RequestBody MemberRoleRequest memberRoleRequest) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.assignRoleForMember(member, memberRoleRequest.roleId());
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{homeId}/members/{userId}/role")
    public ResponseEntity<HmsResponse<Void>> assignRoleForMember(@PathVariable Integer homeId, @PathVariable Integer userId) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.removeRoleForMember(member);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
