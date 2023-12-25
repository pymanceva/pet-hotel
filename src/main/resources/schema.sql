DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS categories;

CREATE TABLE IF NOT EXISTS users
(
    id_users          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    last_name_users   VARCHAR(30),
    first_name_users  VARCHAR(15)                             NOT NULL,
    middle_name_users VARCHAR(15),
    email_users       VARCHAR(254)                            NOT NULL,
    password_users    VARCHAR(10),
    role_users        VARCHAR(16)                             NOT NULL,
    active_users      BOOLEAN DEFAULT TRUE                    NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id_users),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email_users)
);

INSERT INTO users (first_name_users, email_users, password_users, role_users)
values ('boss', 'boss@mail.ru', 'boss_pwd', 'ROLE_BOSS');

CREATE TABLE IF NOT EXISTS pets
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    type_pet         VARCHAR(250)                            NOT NULL,
    breed_pet        VARCHAR(250)                            NOT NULL,
    sex_pet          VARCHAR(13)                             NOT NULL,
    age_pet          INTEGER                                 NOT NULL,
    weight_pet       INTEGER                                 NOT NULL,
    diet_pet         VARCHAR(21)                             NOT NULL,
    medication_pet   BOOLEAN                                 NOT NULL,
    contact_pet      BOOLEAN                                 NOT NULL,
    photographed_pet BOOLEAN                                 NOT NULL,
    comments_pet     VARCHAR(1000),
    CONSTRAINT pk_pet PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id_categories          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name_categories        VARCHAR(20)                             NOT NULL,
    description_categories VARCHAR(250)                                    ,
    CONSTRAINT pk_categories PRIMARY KEY (id_categories),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE(name_categories)
);

CREATE TABLE IF NOT EXISTS rooms
(
    id_rooms          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    number_rooms      VARCHAR(100)                            NOT NULL,
    area_rooms        REAL                                            ,
    category_id_rooms BIGINT                                          ,
    description_rooms VARCHAR(250)                                    ,
    visible_rooms   BOOLEAN DEFAULT TRUE                              ,
    CONSTRAINT pk_rooms PRIMARY KEY (id_rooms),
    CONSTRAINT fk_cat_to_rooms FOREIGN KEY (category_id_rooms) REFERENCES categories(id_categories) ON DELETE RESTRICT,
    CONSTRAINT UQ_ROOM_NUMBER UNIQUE(number_rooms),
    CONSTRAINT positive_room_area CHECK (area_rooms >= 0)
);



