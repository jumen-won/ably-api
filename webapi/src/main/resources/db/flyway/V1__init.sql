CREATE TABLE IF NOT EXISTS user
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255)          NOT NULL,
    password   VARCHAR(255)          NOT NULL,
    created_at datetime(6)           NOT NULL,
    updated_at datetime(6)           NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uk_user_email UNIQUE (email)
);

create table if not exists wish_list
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NOT NULL,
    title      VARCHAR(255)          NOT NULL,
    created_at datetime(6)           NOT NULL,
    updated_at datetime(6)           NOT NULL,
    CONSTRAINT pk_wish_list PRIMARY KEY (id),
    CONSTRAINT uk_wish_list_user_id_title UNIQUE (user_id, title),
    INDEX idx_wish_list_user_id (user_id)
);

create table if not exists wish_item
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    item_code    VARCHAR(255)          NOT NULL,
    wish_list_id BIGINT                NOT NULL,
    user_id      BIGINT                NOT NULL,
    created_at   datetime(6)           NOT NULL,
    updated_at   datetime(6)           NOT NULL,
    CONSTRAINT pk_wish_list_item PRIMARY KEY (id),
    CONSTRAINT uk_wish_item_user_id_item_code UNIQUE (user_id, item_code),
    INDEX idx_wish_item_user_id_wish_list_id (user_id, wish_list_id)
);

create table if not exists item
(
    code       VARCHAR(255)          NOT NULL,
    url        VARCHAR(255)          NOT NULL,
    price      INT                   NOT NULL,
    created_at datetime(6)           NOT NULL,
    updated_at datetime(6)           NOT NULL,
    CONSTRAINT pk_code PRIMARY KEY (code)
);
