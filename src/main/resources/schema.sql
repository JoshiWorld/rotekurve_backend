create table aktuelles
(
    id        int auto_increment
        primary key,
    title     varchar(255) not null,
    message   longtext     not null,
    `release` datetime     not null
);

create table bankdaten
(
    id       int auto_increment
        primary key,
    bankname varchar(100) null,
    iban     varchar(255) not null,
    kontonr  varchar(255) null
);

create table mitglieder
(
    mitgliedsnr int auto_increment
        primary key,
    forename    varchar(50)                              not null,
    lastname    varchar(100)                             not null,
    status      enum ('aktiv', 'passiv') default 'aktiv' not null,
    joindate    date                                     not null,
    email       varchar(255)                             not null
);

create table mitglieder_bankdaten
(
    mitgliedsnr int not null,
    bankdatenid int not null,
    constraint mitglieder_bankdaten_pk
        unique (mitgliedsnr),
    constraint mitglieder_bankdaten_bankdaten_id_fk
        foreign key (bankdatenid) references bankdaten (id),
    constraint mitglieder_bankdaten_mitgliedsnr_fk
        foreign key (mitgliedsnr) references mitglieder (mitgliedsnr)
);

create table user
(
    id       int auto_increment
        primary key,
    username varchar(100) not null,
    password varchar(255) not null,
    email    varchar(255) not null
);

create table aktuelles_von_user
(
    aktuelles_id int not null,
    user_id      int not null,
    constraint aktuelles_von_user_aktuelles_id_fk
        foreign key (aktuelles_id) references aktuelles (id),
    constraint aktuelles_von_user_user_id_fk
        foreign key (user_id) references user (id)
);

create table user_mitglieder
(
    user_id     int not null
        primary key,
    mitgliedsnr int not null,
    constraint user_mitglieder_mitgliedsnr_fk
        foreign key (mitgliedsnr) references mitglieder (mitgliedsnr),
    constraint user_mitglieder_user_id_fk
        foreign key (user_id) references user (id)
);
