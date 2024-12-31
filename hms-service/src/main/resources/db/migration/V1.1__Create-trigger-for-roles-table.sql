DELIMITER //

CREATE DEFINER=`hms_migration_user`@`%` TRIGGER `appuser-can-not-modify-system-role`
    BEFORE UPDATE ON hms_dev.roles
    FOR EACH ROW
BEGIN
    SET @user_name = SUBSTRING_INDEX(SESSION_USER(), '@', 1);
    IF @user_name = 'hmsuser' AND OLD.role_type = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This user cannot modify system role';
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

DELIMITER ;
