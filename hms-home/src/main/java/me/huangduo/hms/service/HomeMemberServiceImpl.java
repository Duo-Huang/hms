package me.huangduo.hms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.HomeMemberRolesDao;
import me.huangduo.hms.dao.HomesDao;
import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dto.model.*;
import me.huangduo.hms.enums.ErrorCode;
import me.huangduo.hms.enums.SystemRole;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.InvitationEventFactory;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.HomeMemberAlreadyExistsException;
import me.huangduo.hms.exceptions.InvitationCodeExpiredException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.mapper.HomeMapper;
import me.huangduo.hms.mapper.MemberMapper;
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

    private final InvitationEventFactory invitationEventFactory;

    private final ObjectMapper objectMapper;

    public HomeMemberServiceImpl(
            ApplicationEventPublisher applicationEventPublisher,
            HomeMemberRolesDao homeMemberRolesDao,
            HomesDao homesDao,
            CommonService commonService,
            HomeMapper homeMapper,
            MemberMapper memberMapper,
            CheckService checkService, InvitationEventFactory invitationEventFactory, ObjectMapper objectMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.homeMemberRolesDao = homeMemberRolesDao;
        this.homesDao = homesDao;
        this.commonService = commonService;
        this.homeMapper = homeMapper;
        this.memberMapper = memberMapper;
        this.checkService = checkService;
        this.invitationEventFactory = invitationEventFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void inviteUser(Integer homeId, User inviter, User invitee) throws RecordNotFoundException, IllegalArgumentException {
        if (invitee == null) {
            throw new IllegalArgumentException("user can not be null.");
        }
        final User inviteeUserInfo = checkService.checkUserExisted(invitee.getUsername()); // check and get all newest user info

        List<HomeMemberRoleEntity> homeMembers = homeMemberRolesDao.getItemsByHomeId(homeId);

        if (homeMembers.stream().anyMatch(x -> x.getUserId().equals(inviteeUserInfo.getUserId()))) {
            BusinessException e = new HomeMemberAlreadyExistsException();
            log.error("This home member is already existed.", e);
            throw e;
        }
        String invitationCode = InvitationCoder.userIdToCode(inviter.getUserId());

        log.info("Ready to send invitations, homeId: {}, invitationCode: {}, inviter username: {}, invitee username: {}", homeId, invitationCode, inviter.getUsername(), inviteeUserInfo.getUsername());

        InvitationEvent invitationEvent = invitationEventFactory.createEvent(inviter, homeId, invitationCode, inviter, inviteeUserInfo);
        applicationEventPublisher.publishEvent(invitationEvent);

    }

    @Override
    @Transactional
    public void acceptInvitation(User invitee, String invitationCode) throws JsonProcessingException {
        if (invitee == null) {
            throw new IllegalArgumentException("user can not be null.");
        }

        Integer inviterHomeId = verifyInvitationCode(invitationCode);

        addMember(inviterHomeId, invitee);
        // assign a default home member role for this user
        me.huangduo.hms.dto.model.SystemRole homeMemberRole = commonService.getSystemRoleByName(SystemRole.HOME_MEMBER);

        if (Objects.isNull(homeMemberRole)) {
            log.warn("The member is not assigned a default home member role for this new member.");
        } else {
            assignRoleForMember(Member.builder().homeId(inviterHomeId).userId(invitee.getUserId()).build(), homeMemberRole.getRoleId());
        }
    }

    private Integer verifyInvitationCode(String invitationCode) throws JsonProcessingException {
        if (invitationCode == null) {
            throw new IllegalArgumentException("invitationCode can not be null");
        }
        // TODO: 校验,用invitationCode和加入家庭type的消息查询消息表,如果消息没过期并且未处理,然后比对被邀请人invitee是否是当前user, 不是就抛IllegalArgumentException

        Message invatationMessage = commonService.getMessageByInvitationCode(invitationCode);

        if (invatationMessage == null) {
            log.error("Can not found invitation message, invitationCode: {}", invitationCode);
            throw new RecordNotFoundException(ErrorCode.HOME_ERROR_2016);
        }

        if (invatationMessage.expiration().isBefore(LocalDateTime.now())) {
            log.error("The invitation message has expired. invitationCode: {}, expiration: {}", invitationCode, invatationMessage.expiration());
            throw new InvitationCodeExpiredException();
        }

        Integer inviterUserIdFromCode = InvitationCoder.codeToUserId(invitationCode);
        InvitationEvent.InvitationMessagePayload messagePayload = null;
        try {
            messagePayload = objectMapper.readValue(invatationMessage.payload(), InvitationEvent.InvitationMessagePayload.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse invitation message payload due to JSON parse error. value: {}", invatationMessage.payload(), e);
            throw e;
        }

        if (!Objects.equals(inviterUserIdFromCode, messagePayload.getInviterUserId())) {
            log.error("inviterUserIdFromCode is not matched. inviterUserIdFromCode: {}, real inviterUserId: {}", inviterUserIdFromCode, messagePayload.getInviterUserId());
            throw new IllegalArgumentException("Invalid invitation code.");
        }

        if (Objects.isNull(homesDao.getById(messagePayload.getHomeId()))) {
            BusinessException e = new RecordNotFoundException(ErrorCode.HOME_ERROR_203);
            log.error("The home of the inviter cannot be found. inviterId: {}, homeId: {}", inviterUserIdFromCode, messagePayload.getHomeId(), e);
            throw e;
        }

        if (!commonService.isUserInHome(messagePayload.getHomeId(), inviterUserIdFromCode)) {
            log.error("The home of the inviter cannot be found. inviterId: {}", inviterUserIdFromCode);
            throw new IllegalArgumentException("Invalid invitation code.");
        }

        return messagePayload.getHomeId();
    }

    @Override
    public void addMember(Integer homeId, User user) throws RecordNotFoundException, HomeMemberAlreadyExistsException, IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null.");
        }
        final User userInfo = checkService.checkUserExisted(user.getUserId()); // check and get all newest user info

        List<HomeMemberRoleEntity> homeMembers = homeMemberRolesDao.getItemsByHomeId(homeId);

        if (homeMembers.stream().anyMatch(x -> x.getUserId().equals(userInfo.getUserId()))) {
            BusinessException e = new HomeMemberAlreadyExistsException();
            log.error("This home member is already existed.", e);
            throw e;
        }

        homeMemberRolesDao.add(
                HomeMemberRoleEntity.builder()
                        .userId(userInfo.getUserId())
                        .homeId(homeId)
                        .memberName(userInfo.getNickname())
                        .build()
        );
    }

    @Override
    public void removeMember(Member member) throws RecordNotFoundException {
        int row = homeMemberRolesDao.removeItemByUserIdAndHomeId(memberMapper.toEntity(member));

        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCode.HOME_ERROR_206);
            log.error("This home member doesn't existed.", e);
            throw e;
        }
    }

    @Override
    public Member getMemberWithRole(Integer homeId, Integer userId) throws RecordNotFoundException {
        Member member = homeMemberRolesDao.getMemberWithRoleByHomeIdAndUserId(homeId, userId);
        if (member == null) {
            BusinessException e = new RecordNotFoundException(ErrorCode.HOME_ERROR_206);
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
            BusinessException e = new RecordNotFoundException(ErrorCode.HOME_ERROR_206);
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
    public void assignRoleForMember(Member member, Integer roleId) throws RecordNotFoundException, IllegalArgumentException {
        if (member == null) {
            throw new IllegalArgumentException("user can not be null.");
        }

        checkService.checkRoleAccess(member.getHomeId(), roleId, false);

        HomeMemberRoleEntity homeMemberRoleEntity = memberMapper.toEntity(member);
        homeMemberRoleEntity.setRoleId(roleId);

        int row = homeMemberRolesDao.updateRoleByUserIdAndHomeId(homeMemberRoleEntity);
        if (row == 0) {
            BusinessException e = new RecordNotFoundException(ErrorCode.HOME_ERROR_206);
            log.error("This home member doesn't exist.", e);
            throw e;
        }
    }

    @Override
    public void removeRoleForMember(Member member) throws RecordNotFoundException, IllegalArgumentException {
        assignRoleForMember(member, null);
    }
}
