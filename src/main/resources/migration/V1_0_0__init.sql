-- Издательство
CREATE TABLE publishing_house
(
    id          serial8 NOT NULL,
    "name"      varchar NOT NULL,
    description varchar NULL,
    create_date timestamp NULL,
    update_date timestamp NULL,
    active      bool    NOT NULL DEFAULT true,
    CONSTRAINT publishing_house_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX publishing_house_name_unq ON publishing_house ("name");
CREATE INDEX publishing_house_active_idx ON publishing_house (active);
COMMENT
ON TABLE publishing_house IS 'Издательство';

-- Тема книги
CREATE TABLE theme
(
    id          serial8 NOT NULL,
    "name"      varchar NOT NULL,
    description varchar NULL,
    create_date timestamp NULL,
    update_date timestamp NULL,
    active      bool    NOT NULL DEFAULT true,
    CONSTRAINT theme_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX theme_name_unq ON theme ("name");
CREATE INDEX theme_active_idx ON theme (active);
COMMENT
ON TABLE theme IS 'Тема книги';

-- Автор книги
CREATE TABLE author
(
    id          serial8 NOT NULL,
    first_name  varchar NOT NULL,
    middle_name varchar NULL,
    last_name   varchar NOT NULL,
    birth_date  date    NOT NULL,
    death_date  date NULL,
    description varchar NULL,
    create_date timestamp NULL,
    update_date timestamp NULL,
    active      bool    NOT NULL DEFAULT true,
    CONSTRAINT author_pk PRIMARY KEY (id)
);
CREATE INDEX author_active_idx ON author (active);
COMMENT
ON TABLE author IS 'Автор книги';

-- Книга
CREATE TABLE book
(
    id                  serial8 NOT NULL,
    publishing_house_id int8    NOT NULL,
    "name"              varchar NOT NULL,
    publish_year        int     NOT NULL,
    amount              int     NOT NULL DEFAULT 0,
    description         varchar NULL,
    create_date         timestamp NULL,
    update_date         timestamp NULL,
    active              bool    NOT NULL DEFAULT true,
    CONSTRAINT book_pk PRIMARY KEY (id),
    CONSTRAINT book_publishing_house_fk FOREIGN KEY (publishing_house_id) REFERENCES publishing_house (id)
);
CREATE INDEX book_active_idx ON book (active);
CREATE INDEX book_amount_idx ON book (amount);
COMMENT
ON TABLE book IS 'Книга';

-- Связь "Книга - Автор"
CREATE TABLE book_author
(
    book_id   int8 NOT NULL,
    author_id int8 NOT NULL,
    CONSTRAINT book_author_pk PRIMARY KEY (book_id, author_id),
    CONSTRAINT book_author_book_fk FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT book_author_author_fk FOREIGN KEY (author_id) REFERENCES author (id)
);
COMMENT
ON TABLE book_author IS 'Связь "Книга - Автор"';

-- Связь "Книга - Тема"
CREATE TABLE book_theme
(
    book_id  int8 NOT NULL,
    theme_id int8 NOT NULL,
    CONSTRAINT book_theme_pk PRIMARY KEY (book_id, theme_id),
    CONSTRAINT book_theme_book_fk FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT book_theme_theme_fk FOREIGN KEY (theme_id) REFERENCES theme (id)
);
COMMENT
ON TABLE book_theme IS 'Связь "Книга - Тема"';

-- Библиотекарь
CREATE TABLE librarian
(
    id              serial8 NOT NULL,
    first_name      varchar NOT NULL,
    middle_name     varchar NULL,
    last_name       varchar NOT NULL,
    phone_number    varchar NOT NULL,
    address         varchar NOT NULL,
    employment_date date    NOT NULL,
    dismissal_date  date NULL,
    create_date     timestamp NULL,
    update_date     timestamp NULL,
    active          bool    NOT NULL DEFAULT true,
    CONSTRAINT librarian_pk PRIMARY KEY (id)
);
CREATE INDEX librarian_active_idx ON librarian (active);
COMMENT
ON TABLE librarian IS 'Библиотекарь';

-- Читатель (абонент)
CREATE TABLE subscriber
(
    id            serial8 NOT NULL,
    first_name    varchar NOT NULL,
    middle_name   varchar NULL,
    last_name     varchar NOT NULL,
    birth_date    date    NOT NULL,
    passport_data varchar NOT NULL,
    phone_number  varchar NOT NULL,
    address       varchar NOT NULL,
    create_date   timestamp NULL,
    update_date   timestamp NULL,
    active        bool    NOT NULL DEFAULT true,
    CONSTRAINT subscriber_pk PRIMARY KEY (id)
);
CREATE INDEX subscriber_active_idx ON subscriber (active);
COMMENT
ON TABLE subscriber IS 'Читатель (абонент)';

-- Журнал выдачи
CREATE TABLE journal
(
    id            serial8 NOT NULL,
    librarian_id  int8    NOT NULL,
    subscriber_id int8    NOT NULL,
    issue_date    date    NOT NULL,
    create_date   timestamp NULL,
    update_date   timestamp NULL,
    active        bool    NOT NULL DEFAULT true,
    CONSTRAINT journal_pk PRIMARY KEY (id),
    CONSTRAINT journal_librarian_fk FOREIGN KEY (librarian_id) REFERENCES librarian (id),
    CONSTRAINT journal_subscriber_fk FOREIGN KEY (subscriber_id) REFERENCES subscriber (id)
);
CREATE INDEX journal_active_idx ON journal (active);
COMMENT
ON TABLE journal IS 'Журнал выдачи';

-- Справочник состояний книги
CREATE TABLE book_state
(
    id          serial8 NOT NULL,
    "name"      varchar NOT NULL,
    description varchar NULL,
    create_date timestamp NULL,
    update_date timestamp NULL,
    active      bool    NOT NULL DEFAULT true,
    CONSTRAINT ref_book_state_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX book_state_name_unq ON book_state ("name");
CREATE INDEX ref_book_state_active_idx ON book_state (active);
COMMENT
ON TABLE book_state IS 'Справочник состояний книги';

-- Связь "Журнал выдачи - Книга"
CREATE TABLE journal_book
(
    journal_id    int8 NOT NULL,
    book_id       int8 NOT NULL,
    return_date   date NOT NULL,
    book_state_id int8 NOT NULL,
    create_date   timestamp NULL,
    update_date   timestamp NULL,
    active        bool NOT NULL DEFAULT true,
    CONSTRAINT journal_book_pk PRIMARY KEY (journal_id, book_id),
    CONSTRAINT journal_book_jornal_fk FOREIGN KEY (journal_id) REFERENCES journal (id),
    CONSTRAINT journal_book_book_fk FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT journal_book_book_state_fk FOREIGN KEY (book_state_id) REFERENCES book_state (id)
);
CREATE INDEX journal_book_active_idx ON journal_book (active);
COMMENT
ON TABLE journal_book IS 'Связь "Журнал выдачи - Книга"';