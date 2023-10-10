
drop database if exists gomoku_grader;
create database gomoku_grader;
use gomoku_grader;

drop table if exists app_user_role;
drop table if exists app_role;
drop table if exists app_user;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default(1),
    first_name text,
    last_name text,
    email text,
    external_id text
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

create table submission (
    submission_id int primary key auto_increment,
    app_user_id int not null,
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
    description text,
    board_state text,
    submission_id int not null,
        constraint fk_test_case_outcome_submission_id
                foreign key (submission_id)
                references submission(submission_id)
);

create table grading_batch (
    grading_batch_id int primary key auto_increment,
    created_at timestamp,
    graded_at timestamp
);
