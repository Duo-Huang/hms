create table revoked_tokens
(
    id         int auto_increment comment 'primary key'
        primary key,
    jti        varchar(60)                         not null comment 'jwt id',
    expiration datetime                            not null comment 'expiration time',
    username   varchar(30)                         not null comment 'foreign key for users',
    created_at datetime  default current_timestamp not null comment 'created time',
    updated_at timestamp default current_timestamp not null on update current_timestamp comment 'updated time',
    constraint `jti-unique`
        unique (username),
    constraint `revoked_tokens-users-username-fk`
        foreign key (username) references users (username)
)
    comment 'logged out tokens';

