DELIMITER //

create definer=`hms_migration_user`@`%` trigger `appuser-can-not-modify-system-role`
    before update on roles
    for each row
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND (OLD.role_type = 0 OR NEW.role_type = 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot modify system role or change a custom role to system role';
    END IF;
END;
//

create definer=`hms_migration_user`@`%` trigger `appuser-can-not-delete-system-role`
    BEFORE DELETE ON roles
    for each row
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND OLD.role_type = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot delete system role';
    END IF;
END;
//

create definer=`hms_migration_user`@`%` trigger `appuser-can-not-create-system-role`
    BEFORE INSERT ON roles
    for each row
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND NEW.role_type = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot create a system role';
    END IF;
END;
//

create definer=`hms_migration_user`@`%` trigger `create-nonsystem-role-must-in-a-home`
    BEFORE INSERT ON roles
    for each row
BEGIN
    IF NEW.role_type != 0 AND NEW.home_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'home_id must be provided when is not a system role';
    END IF;
END;
//

create definer=`hms_migration_user`@`%` trigger `update-nonsystem-role-must-in-a-home`
    before update on roles
    for each row
BEGIN
    IF NEW.role_type != 0 AND NEW.home_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'home_id must be provided when is not a system role';
    END IF;
END;
//

create definer=`hms_migration_user`@`%` trigger `create-invitation-message-must-have-payload`
    before insert on messages
    for each row
BEGIN
    IF NEW.message_type = 1 AND NEW.payload IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'payload must be provided when message is invitation type';
    END IF;
END;
//

create definer=`hms_migration_user`@`%` trigger `update-invitation-message-must-have-payload`
    before update on messages
    for each row
BEGIN
    IF NEW.message_type = 1 AND NEW.payload IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'payload must be provided when message is invitation type';
    END IF;
END;
//

DELIMITER ;
