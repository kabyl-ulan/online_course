insert into users (id, email, name, surname, password, phone_number, role)
VALUES (1, 'admin@gmail.com', 'Admin', 'Adminov', '$2a$12$MOY3XQCNQzwgv4lFqnpSrey/ELGiXhyTQZRygWVlAwFu3Zxy3zFbu',
        '0702003550', 'SUPER_ADMIN'),
       (2, 'client@gmail.com', 'Client', 'Clientov', '$2a$12$PhRg1tBOK9sA2A8gEa4JZ.lF6yWI5/KKg38CkmNsOSTFWQekA72Y6',
        '0702010101', 'USER');

insert into categories (id, category_name, parent_category_id)
VALUES (1, 'Английский язык', null),
       (2, 'Американский', 1),
       (3, 'Британский', 1),
       (4, 'Для детей', 1),
       (5, 'Backend', null),
       (6, 'Java', 5),
       (7, 'Node', 5),
       (8, 'Python', 5);

insert into course (id, lessons_number, description, price, course_name, category_id)
VALUES (1, 5, 'This course will teach about English', 1000, 'Pre-Intermediate English', 1),
        (2, 6, 'This full tutorial about Java core', 1000, 'Java Core', 1);






insert into reviews (id, date, text, course_id, user_id)
VALUES (1, '2022-12-10', 'This is the best picture in my opinion', 1, 2),
       (2, '2022-12-10', 'This is so good', 2, 1);
