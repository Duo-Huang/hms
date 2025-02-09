package me.huangduo.hms.service;

import me.huangduo.hms.model.User;
import me.huangduo.hms.exceptions.RecordNotFoundException;

public interface CheckService {

    User checkUserExisted(Integer userId) throws RecordNotFoundException;

    User checkUserExisted(String username) throws RecordNotFoundException;

    void checkRoleAccess(Integer homeId, Integer roleId, boolean isModification) throws RecordNotFoundException, IllegalArgumentException;
}
