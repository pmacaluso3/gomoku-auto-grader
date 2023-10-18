
drop database if exists gomoku_grader;
create database gomoku_grader;
use gomoku_grader;

drop table if exists app_user_role;
drop table if exists app_role;
drop table if exists app_user;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048),
    enabled bit not null default(0),
    first_name text,
    last_name text,
    external_id text,
    account_setup_token text,
    has_been_setup boolean default false
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table grading_batch (
    grading_batch_id int primary key auto_increment,
    created_at timestamp,
    graded_at timestamp
);

create table submission (
    submission_id int primary key auto_increment,
    app_user_id int not null,
    zip_file blob,
    grading_batch_id int,
    created_at timestamp,
    graded_at timestamp,
    constraint fk_submission_app_user_id
            foreign key (app_user_id)
            references app_user(app_user_id),
    constraint fk_submission_grading_batch_id
            foreign key (grading_batch_id)
            references grading_batch(grading_batch_id)
);

create table test_case_outcome (
    test_case_outcome_id int primary key auto_increment,
    submission_id int not null,
    success boolean not null,
    has_been_manually_edited boolean not null default false,
    description text,
    board_state text,
        constraint fk_test_case_outcome_submission_id
                foreign key (submission_id)
                references submission(submission_id)
);

insert into app_role (`name`) values
    ('ADMIN'),
    ('APPLICANT');


insert into app_user (username, password_hash, enabled)
    values
    ('admin@dev-10.com', '$2a$10$WZPggZQdJ7li5N/x1AwQ3eJo73pzV2803CUoIaGJ4e965pW49c0Y2', 1); -- pw == admin

insert into app_user_role (app_user_id, app_role_id) values (1, 1);














