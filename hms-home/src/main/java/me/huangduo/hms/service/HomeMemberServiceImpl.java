package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.HomeMemberRolesDao;
import me.huangduo.hms.dao.HomesDao;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.enums.ErrorCodeEnum;
import me.huangduo.hms.enums.SystemRoleEnum;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.NotificationEvent;
import me.huangduo.hms.exceptions.*;
import me.huangduo.hms.mapper.HomeMapper;
import me.huangduo.hms.mapper.MemberMapper;
import me.huangduo.hms.model.*;
import me.huangduo.hms.utils.InvitationCoder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeMemberServiceImpl implements HomeMemberService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final HomeMemberRolesDao homeMemberRolesDao;

    private final HomesDao homesDao;

    private final CommonService commonService;

    private final HomeMapper homeMapper;

    private final MemberMapper memberMapper;

    private final CheckService checkService;

    public HomeMemberServiceImpl(
            ApplicationEventPublisher applicationEventPublisher,
            HomeMemberRolesDao homeMemberRolesDao,
            HomesDao homesDao,
            CommonService commonService,
            HomeMapper homeMapper,
            MemberMapper memberMapper,
            CheckService checkService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.homeMemberRolesDao = homeMemberRolesDao;
        this.homesDao = homesDao;
        this.commonService = commonService;
        this.homeMapper = homeMapper;
        this.memberMapper = memberMapper;
        this.checkService = checkService;
    }

    @Override
    public void inviteUser(Integer homeId, User inviter, User invitee) throws RecordNotFoundException, IllegalArgumentException {
        if (invitee == null) {
            throw new IllegalArgumentException("user can not be null.");
        }
        if (Objects.equals(inviter.getUsername(), invitee.getUsername())) {
            throw new IllegalArgumentException("invalid invitee user");
        }

        final User inviteeUserInfo = checkService.checkUserExisted(invitee.getUsername()); // check and get all newest user info

        if (commonService.isUserInHome(homeId, invitee.getUserId())) {
            BusinessException e = new HomeMemberAlreadyExistsException();
            log.error("This home member is already existed.", e);
            throw e;
        }

        String invitationCode = InvitationCoder.userIdToCode(inviter.getUserId());

        log.info("Ready to send invitations, homeId: {}, invitationCode: {}, inviter username: {}, invitee username: {}", homeId, invitationCode, inviter.getUsername(), inviteeUserInfo.getUsername());

        InvitationEvent invitationEvent = new InvitationEvent(this.getClass(), homeId, inviter, invitationCode, inviteeUserInfo);
        applicationEventPublisher.publishEvent(invitationEvent);
    }

    @Override
    @Transactional
    public void acceptInvitation(User invitee, String invitationCode) throws IllegalArgumentException, RecordNotFoundException, InvitationCodeExpiredException, HomeMemberAlreadyExistsException {
        if (invitee == null) {
            throw new IllegalArgumentException("invitee user can not be null.");
        }

        Integer inviterHomeId = verifyInvitationCode(invitationCode);

        if (commonService.isUserInHome(inviterHomeId, invitee.getUserId())) {
            BusinessException e = new HomeMemberAlreadyExistsException();
            log.error("This home member is already existed.", e);
            throw e;
        }

        addMember(inviterHomeId, invitee);
        // assign a default home member role for this user
        SystemRole homeMemberRole = commonService.getSystemRoleByName(SystemRoleEnum.HOME_MEMBER);

        if (Objects.isNull(homeMemberRole)) {
            log.warn("The member is not assigned a default home member role for this new member.");
        } else {
            assignRoleForMember(invitee, Member.builder().homeId(inviterHomeId).userId(invitee.getUserId()).build(), homeMemberRole.getRoleId(), true);
        }

        String message = """
                <#if currentUser.userId == %d>
                  你已加入此家庭<#t>
                <#else>
                  %s 加入家庭了<#t>
                </#if>
                """.formatted(invitee.getUserId(), invitee.getNickname());

        applicationEventPublisher.publishEvent(new NotificationEvent(this.getClass(), inviterHomeId, invitee, message));
    }

    private Integer verifyInvitationCode(String invitationCode) {
        if (invitationCode == null) {
            throw new IllegalArgumentException("invitationCode can not be null");
        }

        Message invatationMessage = commonService.getMessageByInvitationCode(invitationCode);

        if (invatationMessage == null) {
            log.error("Can not found invitation message, invitationCode: {}", invitationCode);
            throw new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_216);
        }

        if (invatationMessage.getExpiration().isBefore(LocalDateTime.now())) {
            log.error("The invitation message has expired. invitationCode: {}, expiration: {}", invitationCode, invatationMessage.getExpiration());
            throw new InvitationCodeExpiredException();
        }

        Integer inviterUserIdFromCode = InvitationCoder.codeToUserId(invitationCode);

        InvitationEvent.InvitationMessagePayload messagePayload = InvitationEvent.InvitationMessagePayload.deserialize(invatationMessage.getPayload(), InvitationEvent.InvitationMessagePayload.class);

        if (!Objects.equals(inviterUserIdFromCode, messagePayload.getPublisher().getUserId())) {
            log.error("inviterUserIdFromCode is not matched. inviterUserIdFromCode: {}, real inviterUserId: {}", inviterUserIdFromCode, messagePayload.getPublisher().getUserId());
            throw new IllegalArgumentException("Invalid invitation code.");
        }

        if (Objects.isNull(homesDao.getById(messagePayload.getHomeId()))) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_203);
            log.error("The home of the inviter cannot be found. inviterId: {}, homeId: {}", inviterUserIdFromCode, messagePayload.getHomeId(), e);
            throw e;
        }

        if (!commonService.isUserInHome(messagePayload.getHomeId(), inviterUserIdFromCode)) {
            log.error("The inviter is not in the home. inviterId: {}", inviterUserIdFromCode);
            throw new IllegalArgumentException("Invalid invitation code.");
        }

        return messagePayload.getHomeId();
    }

    @Override
    public void addMember(Integer homeId, User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null.");
        }
//        final User userInfo = checkService.checkUserExisted(user.getUserId()); // check and get all newest user info

        homeMemberRolesDao.add(
                HomeMemberRoleEntity.builder()
                        .userId(user.getUserId())
                        .homeId(homeId)
                        .memberName(user.getNickname())
                        .build()
        );
    }

    @Override
    public void removeMember(Member member) throws RecordNotFoundException {
        int row = homeMemberRolesDao.removeItemByUserIdAndHomeId(memberMapper.toEntity(member));

        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't existed.", e);
            throw e;
        }
    }

    @Override
    public Member getMemberWithRole(Integer homeId, Integer userId) throws RecordNotFoundException {
        Member member = homeMemberRolesDao.getMemberWithRoleByHomeIdAndUserId(homeId, userId);
        if (member == null) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
        if (member.getRole().getRoleId() == null) {
            member.setRole(null);
        }
        return member;
    }

    @Override
    public void updateMemberInfo(Member member) throws RecordNotFoundException {
        int row = homeMemberRolesDao.updateMemberNameByUserIdAndHomeId(memberMapper.toEntity(member));
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public List<Home> getHomesForUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null.");
        }
        List<Integer> homeIds = homeMemberRolesDao.getHomeIdsByUserId(user.getUserId());
        if (homeIds.isEmpty()) {
            return List.of();
        }
        List<HomeEntity> homeEntities = homesDao.getByIds(homeIds);
        return homeEntities.stream().map(homeMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Member> getMembersWithRoles(Integer homeId) throws RecordNotFoundException {
        List<Member> members = homeMemberRolesDao.getMembersWithRolesByHomeId(homeId);
        members.forEach(x -> {
            if (x.getRole().getRoleId() == null) {
                x.setRole(null);
            }
        });

        return members;
    }

    @Override
    public void assignRoleForMember(User currentUser, Member member, Integer roleId, boolean isInitialAssign) throws RecordNotFoundException, IllegalArgumentException, IllegalAssignRoleException {
        if (member == null) {
            throw new IllegalArgumentException("user can not be null.");
        }

        if (!isInitialAssign && Objects.equals(currentUser.getUserId(), member.getUserId())) {
            throw new IllegalAssignRoleException();
        }

        checkService.checkRoleAccess(member.getHomeId(), roleId, false);

        HomeMemberRoleEntity homeMemberRoleEntity = memberMapper.toEntity(member);
        homeMemberRoleEntity.setRoleId(roleId);

        int row = homeMemberRolesDao.updateRoleByUserIdAndHomeId(homeMemberRoleEntity);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCodeEnum.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public void removeRoleForMember(User currentUser, Member member) throws RecordNotFoundException, IllegalArgumentException {
        assignRoleForMember(currentUser, member, null, false);
    }
}
