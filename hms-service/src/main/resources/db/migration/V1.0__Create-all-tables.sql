create table homes
(
    home_id          int auto_increment comment 'primary key'
        primary key,
    home_name        varchar(60)                        not null comment 'home name',
    home_description varchar(600)                       null comment 'home description',
    created_at       datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time ',
    constraint `home_name-unique`
        unique (home_name)
)
    comment 'hms homes';

create table modules
(
    module_id          int auto_increment comment 'primary key'
        primary key,
    module_name        varchar(60)                        not null comment 'Module name. Naming standard: `xxx-module`',
    module_description varchar(600)                       null comment 'module description',
    module_path        varchar(300)                       not null comment 'module path',
    created_at         datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `module_path-unique`
        unique (module_path)
)
    comment 'hms modules';

create table pages
(
    page_id          int auto_increment comment 'primary key'
        primary key,
    page_name        varchar(60)                        not null comment 'Page name. Naming standard: `xxx-page`',
    page_description varchar(600)                       null comment 'page description',
    page_path        varchar(300)                       not null comment 'page path',
    module_id        int                                not null comment 'foreign key for modules',
    created_at       datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `module_id-page_path-unique`
        unique (module_id, page_path),
    constraint `pages-modules-module_id-fk`
        foreign key (module_id) references modules (module_id)
            on update cascade on delete cascade
)
    comment 'hms all pages';

create table elements
(
    element_id          int auto_increment comment 'primary key'
        primary key,
    element_name        varchar(60)                        not null comment 'Element name. Naming standard: `xxx-xxx-xxx...`',
    element_type        tinyint unsigned                   not null comment 'elemment type, like button(0) or table(1)... ',
    element_description varchar(600)                       null comment 'element description',
    page_id             int                                not null comment 'foreign key for pages',
    created_at          datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at          datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated at',
    constraint `page_id-element_name-element_type-unique`
        unique (page_id, element_name, element_type),
    constraint `elements-pages-page_id-fk`
        foreign key (page_id) references pages (page_id)
            on update cascade on delete cascade
)
    comment 'hms all elements in all pages that need to be access controlled';

create table permissions
(
    permission_id          int auto_increment comment 'primary key'
        primary key,
    permission_name        varchar(60)                          not null comment 'permission name. Naming standard `${moduleName}.${pageName}.${elementName}`',
    permission_description varchar(600)                         not null comment 'permission description',
    module_id              int                                  not null comment 'foreign key for modules',
    page_id                int                                  null comment 'foreign key for pages',
    element_id             int                                  null comment 'foreign key for elements',
    is_enabled             tinyint(1) default 0                 not null comment 'is enable(1) or not(0)',
    unique_hash            varchar(30) as ((case
                                                when (`module_id` is not null) then concat(`module_id`, _utf8mb4'-',
                                                                                           coalesce(`page_id`, _utf8mb4'NULL'),
                                                                                           _utf8mb4'-',
                                                                                           coalesce(`element_id`, _utf8mb4'NULL'))
                                                else NULL end)) comment 'computed column used to confirm a unique row',
    created_at             datetime   default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at             datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `module_id-page_id-element_id-unique`
        unique (unique_hash),
    constraint `permissions-elements-element_id-fk`
        foreign key (element_id) references elements (element_id)
            on update cascade on delete cascade,
    constraint `permissions-modules-module_id-fk`
        foreign key (module_id) references modules (module_id)
            on update cascade on delete cascade,
    constraint `permissions-pages-page_id-fk`
        foreign key (page_id) references pages (page_id)
            on update cascade on delete cascade
)
    comment 'all permission points ';

create definer = hmsuser@`%` trigger `check-page_id-element_id-when-insert`
    before insert
    on permissions
    for each row
begin
    IF NEW.element_id IS NOT NULL AND NEW.page_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'page_id cannot be NULL when element_id has a value';
    END IF;
end;

create definer = hmsuser@`%` trigger `check-page_id-element_id-when-update`
    before update
    on permissions
    for each row
begin
    IF NEW.element_id IS NOT NULL AND NEW.page_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'page_id cannot be NULL when element_id has a value';
    END IF;
end;

create table roles
(
    role_id          int auto_increment comment 'primary key'
        primary key,
    role_type        tinyint unsigned                   not null comment 'system role(0) or custom role(1)',
    role_name        varchar(60)                        not null comment 'role name',
    role_description varchar(600)                       null comment 'role description',
    created_at       datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `role_name-unique`
        unique (role_name)
);

create table users
(
    user_id    int auto_increment comment 'primary key'
        primary key,
    username   varchar(30)                        not null comment 'username for login',
    password   varchar(40)                        not null comment 'password for login',
    nickname   varchar(60)                        null comment 'The full name used for display',
    created_at datetime default CURRENT_TIMESTAMP not null comment 'create time',
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'update time',
    constraint `username-unique`
        unique (username)
)
    comment 'hms users table';

create table user_home_roles
(
    user_home_role_id int auto_increment comment 'primary key'
        primary key,
    user_id           int                                not null comment 'foreign key for users',
    home_id           int                                not null comment 'foreign key for homes',
    role_id           int                                not null comment 'foreign key for roles',
    created_at        datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `user_id-home_id-unique`
        unique (user_id, home_id),
    constraint `user_home_roles-homes-home_id-fk`
        foreign key (home_id) references homes (home_id)
            on update cascade on delete cascade,
    constraint `user_home_roles-roles-role_id-fk`
        foreign key (role_id) references roles (role_id)
            on update cascade on delete cascade,
    constraint `user_home_roles-users-user_id-fk`
        foreign key (user_id) references users (user_id)
            on update cascade on delete cascade
)
    comment 'The role of a user in the home. A user can have only one unique role in a home.';

