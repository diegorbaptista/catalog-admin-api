package com.codemagic.catalog.admin;

import com.codemagic.catalog.admin.domain.video.MediaStatus;
import com.codemagic.catalog.admin.domain.video.Rating;
import net.datafaker.Faker;

import java.time.Year;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String lorem(int chars) {
        return FAKER.lorem().characters(chars);
    }

    public static class Video {

        public static String name() {
            return FAKER.oscarMovie().movieName();
        }

        public static Year launchedAt() {
            return Year.of(Integer.parseInt(FAKER.oscarMovie()
                    .getYear()
                    .replace("A", "")));
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.class);
        }

    }

    public static class ImageMedia {

        public static String name() {
            return FAKER.file().fileName();
        }

        public static String checksum() {
            return FAKER.hashing().md5();
        }

        public static String location() {
            return FAKER.aws().acmARN().concat("/s3://").concat(FAKER.file().fileName());
        }

    }

    public static class AudioMediaVideo {

        public static String name() {
            return FAKER.file().fileName();
        }

        public static String checksum() {
            return FAKER.hashing().md5();
        }

        public static String location() {
            return FAKER.aws().acmARN().concat("/s3://").concat(FAKER.file().fileName());
        }

        public static MediaStatus status() {
            return FAKER.options().option(MediaStatus.class);
        }

    }

}
