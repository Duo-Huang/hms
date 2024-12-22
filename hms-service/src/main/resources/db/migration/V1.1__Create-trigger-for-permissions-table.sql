DELIMITER //

CREATE DEFINER=`hmsuser`@`%` TRIGGER `check-page_id-element_id-when-insert`
    BEFORE INSERT ON permissions
    FOR EACH ROW
BEGIN
    IF NEW.element_id IS NOT NULL AND NEW.page_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'page_id cannot be NULL when element_id has a value';
    END IF;
END;

//

CREATE DEFINER=`hmsuser`@`%` TRIGGER `check-page_id-element_id-when-update`
    BEFORE UPDATE ON permissions
    FOR EACH ROW
BEGIN
    IF NEW.element_id IS NOT NULL AND NEW.page_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'page_id cannot be NULL when element_id has a value';
    END IF;
END;
//

DELIMITER ;
