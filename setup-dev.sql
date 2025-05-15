SET @db_name = 'hms_dev'; -- do not use any special characters
SET @migration_user = 'hms_migration_user';
SET @migration_password = 'hms.dev.migration.1234';
SET @app_user = 'hmsuser';
SET @app_password = 'hms.dev.123';

-- create db
SET @create_db_sql = CONCAT(
        'CREATE DATABASE IF NOT EXISTS ', @db_name,
        ' CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci'
                     );
PREPARE stmt FROM @create_db_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- create migration user
SET @create_migration_user_sql = CONCAT('CREATE USER ''', @migration_user, '''@''%'' IDENTIFIED BY ''', @migration_password, ''';');
PREPARE stmt FROM @create_migration_user_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- grant normal access for migration user
SET @grant_privileges_sql = CONCAT('GRANT ALL PRIVILEGES ON `', @db_name, '`.* TO ''', @migration_user, '''@''%''');
PREPARE stmt FROM @grant_privileges_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- grant SUPER access for migration user to create trigger
SET @grant_super_sql = CONCAT('GRANT SUPER ON *.* TO ''', @migration_user, '''@''%''');
PREPARE stmt FROM @grant_super_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

FLUSH PRIVILEGES;

-- drop app user to remove all default permissions
-- SET @drop_app_user_sql = CONCAT('DROP USER IF EXISTS ''', @app_user, '''@''%'';');
-- PREPARE stmt FROM @drop_app_user_sql;
-- EXECUTE stmt;
-- DEALLOCATE PREPARE stmt;


-- create app user
SET @create_app_user_sql = CONCAT('CREATE USER ''', @app_user, '''@''%'' IDENTIFIED BY ''', @app_password, ''';');
PREPARE stmt FROM @create_app_user_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- grant access
SET @grant_app_privileges_sql = CONCAT('GRANT SELECT, INSERT, UPDATE, DELETE ON `', @db_name, '`.* TO ''', @app_user, '''@''%''');
PREPARE stmt FROM @grant_app_privileges_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

FLUSH PRIVILEGES;
