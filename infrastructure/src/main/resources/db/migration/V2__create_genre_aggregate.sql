create table genres (
    id varchar(36) not null primary key,
    name varchar(255) not null,
    active boolean not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    deleted_at datetime(6)
);

create table genres_categories (
    genre_id varchar(36) not null,
    category_id varchar(36) not null,
    constraint idx_genres_categories unique (genre_id, category_id),
    constraint fk_genres_categories_genre_id foreign key (genre_id) references genres(id) on delete cascade,
    constraint fk_genres_categories_category_id foreign key (category_id) references categories(id) on delete cascade
);
