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
import me.huangduo.hms.dto.request.MemberRoleRequest;
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
@RequestMapping("/members")
public class HomeMemberController {

    private final HomeMemberService homeMemberService;

    private final MemberMapper memberMapper;

    private final HomeMapper homeMapper;

    public HomeMemberController(HomeMemberService homeMemberService, MemberMapper memberMapper, HomeMapper homeMapper) {
        this.homeMemberService = homeMemberService;
        this.memberMapper = memberMapper;
        this.homeMapper = homeMapper;
    }

    @PostMapping("/invite")
    public ResponseEntity<HmsResponse<Void>> inviteMember(@RequestAttribute Integer homeId, @Valid @RequestBody MemberInvitationRequest memberInvitationRequest) {
        User user = new User();
        user.setUsername(memberInvitationRequest.username());
        try {
            homeMemberService.inviteMember(homeId, user);
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum().getCode(), e.getHmsErrorCodeEnum().getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<HmsResponse<List<MemberResponse>>> getMembersForHome(@RequestAttribute Integer homeId) {
        List<Member> members = homeMemberService.getMembersWithRoles(homeId);

        return ResponseEntity.ok(HmsResponse.success(members.stream().map(memberMapper::toResponse).collect(Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity<HmsResponse<Void>> acceptInvitation(@RequestAttribute Integer homeId, @RequestAttribute UserToken userToken) {
        try {
            homeMemberService.addMember(homeId, userToken.userInfo());
            return ResponseEntity.ok(HmsResponse.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponse.error(e.getHmsErrorCodeEnum().getCode(), e.getHmsErrorCodeEnum().getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HmsResponse<Void>> removeMember(@RequestAttribute Integer homeId, @PathVariable Integer userId) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.removeMember(member);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<HmsResponse<MemberResponse>> getMemberInfo(@RequestAttribute Integer homeId, @PathVariable Integer userId) {
        User user = new User();
        user.setUserId(userId);
        Member member = homeMemberService.getMemberWithRole(homeId, user);

        return ResponseEntity.ok(HmsResponse.success(memberMapper.toResponse(member)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HmsResponse<Void>> updateMemberInfo(@RequestAttribute Integer homeId, @PathVariable Integer userId, @Valid @RequestBody MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberMapper.toModel(memberInfoUpdateRequest);
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.updateMemberInfo(member);
        return ResponseEntity.ok(HmsResponse.success());
    }

    @GetMapping("/homes")
    public ResponseEntity<HmsResponse<List<HomeInfoResponse>>> getHomesForCurrentUser(@RequestAttribute UserToken userToken) {
        List<Home> homes = homeMemberService.getHomesForUser(Member.builder().userId(userToken.userInfo().getUserId()).build());
        return ResponseEntity.ok(HmsResponse.success(homes.stream().map(homeMapper::toResponse).collect(Collectors.toList())));
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<HmsResponse<Void>> assignRoleForMember(@PathVariable Integer homeId, @PathVariable Integer userId, @Valid @RequestBody MemberRoleRequest memberRoleRequest) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.assignRoleForMember(member, memberRoleRequest.roleId());
        return ResponseEntity.ok(HmsResponse.success());
    }

    @DeleteMapping("/{userId}/role")
    public ResponseEntity<HmsResponse<Void>> assignRoleForMember(@PathVariable Integer homeId, @PathVariable Integer userId) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.removeRoleForMember(member);
        return ResponseEntity.ok(HmsResponse.success());
    }
}
