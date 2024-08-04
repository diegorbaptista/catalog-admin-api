package com.codemagic.catalog.admin.application;

import com.codemagic.catalog.admin.domain.castmember.CastMember;
import com.codemagic.catalog.admin.domain.castmember.CastMemberType;
import com.codemagic.catalog.admin.domain.category.Category;
import com.codemagic.catalog.admin.domain.genre.Genre;
import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Resource;
import com.codemagic.catalog.admin.domain.video.Video;
import net.datafaker.Faker;

import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String lorem(int count) {
        return FAKER.lorem().characters(count);
    }

    public static final class Categories {
        private static final Category MOVIE = Category.newCategory("Movies", "The most watched movies");
        private static final Category SERIES = Category.newCategory("Series", "The most watched series");
        private static final Category DOCUMENTARIES = Category.newCategory("Documentaries", "The most watched documentaries");

        public static Category movies() {
            return MOVIE.clone();
        }

        public static Category series() {
            return SERIES.clone();
        }

        public static Category documentaries() {
            return DOCUMENTARIES.clone();
        }

        public static Set<Category> categories() {
            return Set.of(movies(), series(), documentaries());
        }
    }

    public static final class Genres {
        private static final Genre ACTION = Genre.newGenre("Action");
        private static final Genre DRAMA = Genre.newGenre("Drama");
        private static final Genre COMEDY = Genre.newGenre("Comedy");
        private static final Genre HORROR = Genre.newGenre("Horror");
        private static final Genre MUSICAL = Genre.newGenre("Musical");

        public static Genre action() {
            return Genre.with(ACTION);
        }

        public static Genre drama() {
            return Genre.with(DRAMA);
        }

        public static Genre comedy() {
            return Genre.with(COMEDY);
        }

        public static Genre horror() {
            return Genre.with(HORROR);
        }

        public static Genre musical() {
            return Genre.with(MUSICAL);
        }

        public static Set<Genre> genres() {
            return Set.of(action(), drama(), comedy(), horror(), musical());
        }
    }

    public static final class CastMembers {

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember member() {
            return CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        }

        public static Set<CastMember> members(final int count) {
            var members = new HashSet<CastMember>();

            for(int index = 0; index < count; index++) {
                members.add(CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()));
            }
            return members;
        }

    }

    public static final class Videos {

        private static final Video MOVIE = Video.newVideo(
                title(),
                description(),
                Year.of(launchedAt()),
                duration(),
                rating(),
                opened(),
                published(),
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        public static Video video() {
            return Video.with(MOVIE);
        }

        public static String title() {
            return FAKER.oscarMovie().movieName();
        }

        public static String description() {
            return FAKER.movie().quote();
        }

        public static int launchedAt() {
            return Integer.parseInt(FAKER.oscarMovie().getYear().replace("A", ""));
        }

        public static double duration() {
            return FAKER.random().nextDouble(80, 150);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.class);
        }

        public static boolean opened() {
            return FAKER.random().nextBoolean();
        }

        public static boolean published() {
            return FAKER.random().nextBoolean();
        }

        public static Resource resource(final Resource.Type type) {
            final var contentType = Stream.of(type)
                    .anyMatch(t -> t.equals(Resource.Type.TRAILER) || t.equals(Resource.Type.VIDEO))
                    ? "video/mp4" : "image/jpeg";
            final var content = FAKER.avatar().image().getBytes();

            return Resource.with(content, contentType, type.name(), type);
        }
    }

}
