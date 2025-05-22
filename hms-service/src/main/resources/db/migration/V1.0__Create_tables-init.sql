create table homes
(
    home_id          int auto_increment comment 'primary key'
        primary key,
    home_name        varchar(30)                        not null comment 'home name',
    home_description varchar(60)                       null comment 'home description',
    created_at       datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time'
)
    comment 'hms homes';

create table messages
(
    message_id      int auto_increment comment 'primary key'
        primary key,
    message_type    tinyint unsigned                   not null comment '0 - notification, 1 - invitation msg...',
    message_content varchar(500)                       not null comment 'message content',
    payload         json                               not null comment 'extra info',
    expiration      datetime                           not null comment 'expiration time',
    created_at      datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time'
)
    comment 'Home notification messages';

create table permissions
(
    permission_id          int auto_increment comment 'primary key'
        primary key,
    permission_code        varchar(200)                       not null comment 'format: {module}:{function}:{action}, for example: financial:account:edit; action must be create/edit/view/delete',
    permission_description varchar(300)                       not null comment 'permission description',
    created_at             datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at             datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `permissions_code-unique`
        unique (permission_code)
)
    comment 'permissions for roles';

create table roles
(
    role_id          int auto_increment comment 'primary key'
        primary key,
    role_type        tinyint unsigned                   not null comment 'system role(0) or custom role(1)',
    role_name        varchar(30)                        not null comment 'role name',
    role_description varchar(60)                       null comment 'role description',
    home_id          int                                null comment 'foreign key for homes',
    created_at       datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `role_name-home_id-unique`
        unique (role_name, home_id),
    constraint `roles-homes-home_id-fk`
        foreign key (home_id) references homes (home_id)
            on update cascade on delete cascade,
    constraint check__role_type
        check (`role_type` in (0, 1))
);

create table role_permissions
(
    role_permission_id int auto_increment comment 'primary key'
        primary key,
    role_id            int                                not null comment 'foreign key for roles',
    permission_id      int                                not null comment 'foreign key for permissions',
    created_at         datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `role_id-permissions_id-unique`
        unique (role_id, permission_id),
    constraint `role_permissions-permissions-permission_id-fk`
        foreign key (permission_id) references permissions (permission_id)
            on update cascade on delete cascade,
    constraint `role_permissions-roles-role_id-fk`
        foreign key (role_id) references roles (role_id)
            on update cascade on delete cascade
)
    comment 'role permissins relationship';

create table users
(
    user_id    int auto_increment comment 'primary key'
        primary key,
    username   varchar(30)                        not null comment 'username for login',
    password   varchar(30)                        not null comment 'password for login',
    nickname   varchar(30)                        null comment 'The full name used for display',
    created_at datetime default CURRENT_TIMESTAMP not null comment 'create time',
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'update time',
    constraint `username-unique`
        unique (username)
)
    comment 'hms users table';

create table home_member_roles
(
    member_id   int auto_increment comment 'primary key'
        primary key,
    user_id     int                                not null comment 'foreign key for users',
    home_id     int                                not null comment 'foreign key for homes',
    role_id     int                                null comment 'foreign key for roles',
    member_name varchar(30)                        null comment 'home member name',
    created_at  datetime default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `user_id-home_id-unique`
        unique (user_id, home_id),
    constraint `user_home_roles-homes-home_id-fk`
        foreign key (home_id) references homes (home_id)
            on update cascade on delete cascade,
    constraint `user_home_roles-roles-role_id-fk`
        foreign key (role_id) references roles (role_id)
            on update set null on delete set null,
    constraint `user_home_roles-users-user_id-fk`
        foreign key (user_id) references users (user_id)
            on update cascade on delete cascade
)
    comment 'The role of a user in the home. A user can have only one unique role in a home.';

create table message_status
(
    message_status_id int auto_increment comment 'primary key'
        primary key,
    user_id           int                                        not null comment 'foreign key for users',
    message_id        int                                        not null comment 'foreign key for messages',
    status            tinyint unsigned default '0'               not null comment 'message status for a user, 0 - unread, 1 - read ...',
    created_at        datetime                                   not null comment 'created time',
    updated_at        datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `message_status-messages-message_id-fk`
        foreign key (message_id) references messages (message_id)
            on update cascade on delete cascade,
    constraint `message_status-users-user_id-fk`
        foreign key (user_id) references users (user_id)
            on update cascade on delete cascade
)
    comment 'message status';

create table revoked_tokens
(
    id         int auto_increment comment 'primary key'
        primary key,
    jti        varchar(60)                         not null comment 'jwt id',
    expiration datetime                            not null comment 'expiration time',
    user_id    int                                 not null comment 'foreign key for users',
    created_at datetime  default CURRENT_TIMESTAMP not null comment 'created time',
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'updated time',
    constraint `jti-unique`
        unique (jti),
    constraint `revoked_tokens-users-user_id-fk`
        foreign key (user_id) references users (user_id)
            on update cascade on delete cascade
)
    comment 'logged out tokens';
