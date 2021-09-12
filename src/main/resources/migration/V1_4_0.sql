INSERT INTO "book" (publishing_house_id, name, publish_year, amount, in_stock, description)
VALUES (1, 'mus. Proin vel nisl. Quisque', '2020', 2, 'true', 'Curabitur egestas nunc sed libero. Proin sed turpis nec'),
       (1, 'dolor elit, pellentesque a, facilisis', '2021', 71, 'true', 'Morbi accumsan laoreet ipsum. Curabitur consequat, lectus sit amet luctus'),
       (3, 'felis. Donec tempor, est ac', '2020', 46, 'true', 'Cras eget nisi dictum augue malesuada malesuada. Integer id magna et'),
       (4, 'nisi a odio semper cursus.', '2021', 32, 'true', 'gravida. Praesent eu nulla at sem molestie sodales. Mauris blandit enim consequat purus. Maecenas libero'),
       (4, 'mi pede, nonummy ut, molestie', '2020', 65, 'true', 'nulla. Integer vulputate, risus'),
       (3, 'sollicitudin orci sem eget massa.', '2020', 14, 'true', 'odio a purus. Duis elementum, dui'),
       (3, 'nunc, ullamcorper eu, euismod ac,', '2021', 59, 'true', 'lorem, auctor quis, tristique ac, eleifend vitae, erat. Vivamus nisi. Mauris'),
       (2, 'sagittis lobortis mauris. Suspendisse aliquet', '2020', 46, 'true', 'et magnis dis parturient montes, nascetur ridiculus mus. Donec dignissim'),
       (4, 'blandit enim consequat purus. Maecenas', '2021', 27, 'true', 'massa. Suspendisse eleifend. Cras sed leo. Cras vehicula aliquet libero.'),
       (1, 'Quisque ac libero nec ligula', '2020', 41, 'true', 'sit amet, faucibus ut, nulla. Cras eu tellus eu augue porttitor interdum.');

INSERT INTO book_author (book_id, author_id)
VALUES (1, 100), (2, 32), (2, 87), (3, 1), (4, 1), (4, 47), (5, 45),
       (6, 71), (6, 57), (7, 37), (8, 98), (9, 100), (9, 99), (10, 10);

INSERT INTO book_theme (book_id, theme_id)
VALUES (1, 5), (2, 3), (2, 2), (3, 1), (4, 4), (4, 3), (5, 4),
       (6, 2), (6, 1), (7, 5), (8, 1), (9, 2), (9, 3), (10, 4);

INSERT INTO journal
(librarian_id, subscriber_id, issue_date, create_date, update_date, active)
VALUES (54, 23, '2021-09-09', '2021-09-09 22:30:21', null, true),
       (94, 3, '2021-05-09', '2021-05-09 22:30:21', null, true),
       (14, 7, '2021-09-10', '2021-09-10 22:30:21', null, true),
       (1, 45, '2021-01-28', '2021-01-28 22:30:21', null, true),
       (34, 1, '2020-09-09', '2020-09-09 22:30:21', null, true);

INSERT INTO journal_item
(journal_id, book_id, return_date, book_state_id, create_date, update_date, active)
VALUES (1, 1, NULL, NULL, '2021-05-09 12:30:21', NULL, true),
       (1, 2, NULL, NULL, '2021-05-09 12:30:21', NULL, true),
       (1, 3, NULL, NULL, '2021-05-09 12:30:21', NULL, true),
       (1, 4, NULL, NULL, '2021-05-09 12:30:21', NULL, true),
       (2, 10, NULL, NULL, '2021-09-11 10:12:21', NULL, true),
       (2, 9, NULL, NULL, '2021-09-11 10:12:21', NULL, true),
       (2, 8, NULL, NULL, '2021-09-11 10:12:21', NULL, true),
       (3, 8, NULL, NULL, '2021-09-01 14:36:00', NULL, true),
       (3, 4, NULL, NULL, '2021-09-01 14:36:00', NULL, true),
       (3, 5, NULL, NULL, '2021-09-01 14:36:00', NULL, true),
       (3, 6, NULL, NULL, '2021-09-01 14:36:00', NULL, true),
       (4, 8, NULL, NULL, '2021-09-05 15:13:22', NULL, true),
       (4, 7, NULL, NULL, '2021-09-05 15:13:22', NULL, true),
       (4, 10, NULL, NULL, '2021-09-05 15:13:22', NULL, true),
       (4, 9, NULL, NULL, '2021-09-05 15:13:22', NULL, true),
       (5, 8, NULL, NULL, '2021-09-09 10:00:21', NULL, true),
       (5, 2, NULL, NULL, '2021-09-09 10:00:21', NULL, true),
       (5, 3, NULL, NULL, '2021-09-09 10:00:21', NULL, true),
       (5, 4, NULL, NULL, '2021-09-09 10:00:21', NULL, true),
       (5, 5, NULL, NULL, '2021-09-09 10:00:21', NULL, true),
       (5, 6, NULL, NULL, '2021-09-09 10:00:21', NULL, true);
