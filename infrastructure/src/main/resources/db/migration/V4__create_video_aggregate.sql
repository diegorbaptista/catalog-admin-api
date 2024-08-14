create table videos_video_media (
    id char(32) not null,
    name varchar(255) not null,
    checksum varchar(255) not null,
    local_path varchar(500) not null,
    encoded_path varchar(500),
    status varchar(30) not null,
    constraint pk_videos_video_media primary key(id)
);

create table videos_image_media (
    id char(32) not null,
    name varchar(255) not null,
    local_path varchar(500) not null,
    constraint pk_videos_image_media primary key(id)
);

create table videos (
    id char(32) not null,
    title varchar(255) not null,
    description varchar(4000),
    launch_year int not null,
    rating varchar(6) not null,
    duration decimal(5,2),
    opened boolean not null,
    published boolean not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    trailer_id char(32),
    video_id char(32),
    banner_id char(32),
    thumbnail_id char(32),
    thumbnail_half_id char(32),
    constraint pk_videos_id primary key (id),
    constraint fk_videos_trailer_id foreign key (trailer_id) references videos_video_media(id),
    constraint fk_videos_video_id foreign key (video_id) references videos_video_media(id),
    constraint fk_videos_banner_id foreign key (banner_id) references videos_image_media(id),
    constraint fk_videos_thumbnail_id foreign key (thumbnail_id) references videos_image_media(id),
    constraint fk_videos_thumbnail_half_id foreign key (thumbnail_half_id) references videos_image_media(id)
);

create index idx_videos_title on videos(title);
create index idx_videos_rating on videos(rating);
create index idx_videos_launch_year on videos(launch_year);

create table videos_categories (
    video_id char(32) not null,
    category_id char(32) not null,
constraint idx_videos_categories unique(video_id, category_id),
constraint fK_videos_categories_video_id foreign key(video_id) references videos(id) on delete cascade,
constraint fK_videos_categories_category_id foreign key(category_id) references categories(id) on delete cascade
);

create table videos_genres (
    video_id char(32) not null,
    genre_id char(32) not null,
constraint idx_videos_genres unique(video_id, genre_id),
constraint fK_videos_genres_video_id foreign key(video_id) references videos(id) on delete cascade,
constraint fK_videos_genres_genre_id foreign key(genre_id) references genres(id) on delete cascade
);

create table videos_cast_members (
    video_id char(32) not null,
    cast_member_id char(32) not null,
constraint idx_videos_cast_members unique(video_id, cast_member_id),
constraint fK_videos_cast_members_video_id foreign key(video_id) references videos(id) on delete cascade,
constraint fK_videos_cast_members_cast_member_id foreign key(cast_member_id) references cast_members(id) on delete cascade
);




