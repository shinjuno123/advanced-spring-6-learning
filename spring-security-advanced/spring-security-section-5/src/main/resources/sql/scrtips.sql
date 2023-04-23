DROP DATABASE IF EXISTS `spring-security-test`;
DROP USER IF EXISTS `securityadmin`@`%`;
CREATE DATABASE IF NOT EXISTS `spring-security-test` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `securityadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `spring-security-test`.* TO `securityadmin`@`%`;


create table customer
(
    id int not null auto_increment,
    email varchar(45) not null,
    pwd varchar(200) not null,
    role varchar(45) not null,
    primary key (id)
);

create table users
(
    id int not null auto_increment,
    username varchar(50) not null,
    password varchar(500) not null,
    enabled  boolean not null,
    primary key (id)
);

create table authorities
(
    id int not null auto_increment,
    username  varchar(50) not null,
    authority varchar(50) not null,
    primary key (id)
);

insert ignore into `users` values(null, 'happy', '12345', '1');
insert ignore into `authorities` values(null, 'happy', 'write');

insert into customer (email, pwd, role)
    values ('shinjuno123@naver.com', '12345', 'admin');