CREATE TABLE reservation_time
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    start_at VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    email VARCHAR(45) NOT NULL,
    password VARCHAR(45) NOT NULL,
    name VARCHAR(30) NOT NULL,
    role VARCHAR(15) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    date VARCHAR(255) NOT NULL,
    time_id BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES  member (id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);


INSERT INTO member (email, password, name, role) VALUES ('test@test.com', 'password', 'test', 'member');
INSERT INTO member (email, password, name, role) VALUES ('admin@test.com', 'password', 'admin', 'admin');
