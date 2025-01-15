DELIMITER //

CREATE DEFINER=`hms_migration_user`@`%` TRIGGER `appuser-can-not-modify-system-role`
    BEFORE UPDATE ON hms_dev.roles
    FOR EACH ROW
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND (OLD.role_type = 0 OR NEW.role_type = 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot modify system role or change a custom role to system role';
    END IF;
END;
//

CREATE DEFINER=`hms_migration_user`@`%` TRIGGER `appuser-can-not-delete-system-role`
    BEFORE DELETE ON hms_dev.roles
    FOR EACH ROW
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND OLD.role_type = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot delete system role';
    END IF;
END;
//

CREATE DEFINER=`hms_migration_user`@`%` TRIGGER `appuser-can-not-create-system-role`
    BEFORE INSERT ON hms_dev.roles
    FOR EACH ROW
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND NEW.role_type = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot create a system role';
    END IF;
END;
//

CREATE DEFINER=`hms_migration_user`@`%` TRIGGER `create-nonsystem-role-must-in-a-home`
    BEFORE INSERT ON hms_dev.roles
    FOR EACH ROW
BEGIN
    IF NEW.role_type != 0 AND NEW.home_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'home_id must be provided when is not a system role';
    END IF;
END;
//

CREATE DEFINER=`hms_migration_user`@`%` TRIGGER `update-nonsystem-role-must-in-a-home`
    BEFORE UPDATE ON hms_dev.roles
    FOR EACH ROW
BEGIN
    IF NEW.role_type != 0 AND NEW.home_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'home_id must be provided when is not a system role';
    END IF;
END;
//

DELIMITER ;
