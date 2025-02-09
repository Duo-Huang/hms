package me.huangduo.hms.controller;

import jakarta.validation.Valid;
import me.huangduo.hms.annotations.ValidId;
import me.huangduo.hms.model.Home;
import me.huangduo.hms.model.Member;
import me.huangduo.hms.model.User;
import me.huangduo.hms.dto.request.MemberInfoUpdateRequest;
import me.huangduo.hms.dto.request.MemberInvitationRequest;
import me.huangduo.hms.dto.request.MemberRoleRequest;
import me.huangduo.hms.dto.request.UserAcceptHomeInvitationRequest;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import me.huangduo.hms.dto.response.MemberResponse;
import me.huangduo.hms.exceptions.HomeAlreadyExistsException;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.InvitationCodeExpiredException;
import me.huangduo.hms.mapper.HomeMapper;
import me.huangduo.hms.mapper.MemberMapper;
import me.huangduo.hms.service.HomeMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/members")
@Validated
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
    public ResponseEntity<HmsResponseBody<Void>> inviteMember(@RequestAttribute Integer homeId, @RequestAttribute User userInfo, @Valid @RequestBody MemberInvitationRequest memberInvitationRequest) {
        User user = new User();
        user.setUsername(memberInvitationRequest.username());
        try {
            homeMemberService.inviteUser(homeId, userInfo, user);
            return ResponseEntity.ok(HmsResponseBody.success());
        } catch (HomeAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponseBody.error(e.getErrorCodeEnum()));
        }
    }

    @GetMapping
    public ResponseEntity<HmsResponseBody<List<MemberResponse>>> getMembersForHome(@RequestAttribute Integer homeId) {
        List<Member> members = homeMemberService.getMembersWithRoles(homeId);

        return ResponseEntity.ok(HmsResponseBody.success(members.stream().map(memberMapper::toResponse).collect(Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity<HmsResponseBody<Void>> acceptInvitation(@RequestAttribute User userInfo, @Valid @RequestBody UserAcceptHomeInvitationRequest userAcceptHomeInvitationRequest) {
        try {
            homeMemberService.acceptInvitation(userInfo, userAcceptHomeInvitationRequest.invitationCode());
            return ResponseEntity.ok(HmsResponseBody.success());
        } catch (HomeMemberAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(HmsResponseBody.error(e.getErrorCodeEnum()));
        } catch (InvitationCodeExpiredException e) {
            return ResponseEntity.status(HttpStatus.GONE).body(HmsResponseBody.error(e.getErrorCodeEnum()));
        }
    }

    @DeleteMapping("/{userId:\\d+}")
    public ResponseEntity<HmsResponseBody<Void>> removeMember(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer userId) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.removeMember(member);
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @GetMapping("/{userId:\\d+}")
    public ResponseEntity<HmsResponseBody<MemberResponse>> getMemberInfo(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer userId) {
        Member member = homeMemberService.getMemberWithRole(homeId, userId);

        return ResponseEntity.ok(HmsResponseBody.success(memberMapper.toResponse(member)));
    }

    @PutMapping("/{userId:\\d+}")
    public ResponseEntity<HmsResponseBody<Void>> updateMemberInfo(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer userId, @Valid @RequestBody MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberMapper.toModel(memberInfoUpdateRequest);
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.updateMemberInfo(member);
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @GetMapping("/homes")
    public ResponseEntity<HmsResponseBody<List<HomeInfoResponse>>> getHomesForCurrentUser(@RequestAttribute User userInfo) {
        List<Home> homes = homeMemberService.getHomesForUser(Member.builder().userId(userInfo.getUserId()).build());
        return ResponseEntity.ok(HmsResponseBody.success(homes.stream().map(homeMapper::toResponse).collect(Collectors.toList())));
    }

    @PutMapping("/{userId:\\d+}/role")
    public ResponseEntity<HmsResponseBody<Void>> assignRoleForMember(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer userId, @Valid @RequestBody MemberRoleRequest memberRoleRequest) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.assignRoleForMember(member, memberRoleRequest.roleId());
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @DeleteMapping("/{userId:\\d+}/role")
    public ResponseEntity<HmsResponseBody<Void>> removeRoleForMember(@RequestAttribute Integer homeId, @ValidId @PathVariable Integer userId) {
        Member member = new Member();
        member.setHomeId(homeId);
        member.setUserId(userId);
        homeMemberService.removeRoleForMember(member);
        return ResponseEntity.ok(HmsResponseBody.success());
    }
}
