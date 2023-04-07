CREATE TABLE genres (
    genre_id int PRIMARY KEY,
    genre_name varchar(50) NOT NULL
);

CREATE TABLE mpas (
    mpa_id int PRIMARY KEY,
    mpa_name varchar(50) NOT NULL
);

CREATE TABLE films (
    film_id int PRIMARY KEY,
    film_name varchar(50) NOT NULL,
    description varchar(200),
    release_date datetime NOT NULL,
    duration int NOT NULL,
    mpa_id int NOT NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpas(mpa_id)
);

CREATE TABLE film_genre (
    film_id int,
    genre_id int,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);

CREATE TABLE users (
    user_id int PRIMARY KEY,
    user_name varchar(50) NOT NULL,
    email varchar(100) UNIQUE NOT NULL,
    login varchar(100) UNIQUE NOT NULL,
    birthday datetime NOT NULL
);

CREATE TABLE likes (
    film_id int,
    user_id int,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE friends (
    user_id int,
    friend_id int,
    confirmed boolean,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id)
);