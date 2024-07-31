package com.codemagic.catalog.admin.application.video.create;

import com.codemagic.catalog.admin.domain.Identifier;
import com.codemagic.catalog.admin.domain.castmember.CastMemberGateway;
import com.codemagic.catalog.admin.domain.castmember.CastMemberID;
import com.codemagic.catalog.admin.domain.category.CategoryGateway;
import com.codemagic.catalog.admin.domain.category.CategoryID;
import com.codemagic.catalog.admin.domain.exceptions.InternalErrorException;
import com.codemagic.catalog.admin.domain.exceptions.NotificationException;
import com.codemagic.catalog.admin.domain.genre.GenreGateway;
import com.codemagic.catalog.admin.domain.genre.GenreID;
import com.codemagic.catalog.admin.domain.validation.Error;
import com.codemagic.catalog.admin.domain.validation.ValidationHandler;
import com.codemagic.catalog.admin.domain.validation.handler.Notification;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.Rating;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoGateway;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final MediaResourceGateway resourceGateway;

    public DefaultCreateVideoUseCase(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final MediaResourceGateway resourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.resourceGateway = Objects.requireNonNull(resourceGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoCommand command) {
        final var rating = Rating.of(command.rating()).orElse(null);
        final var launchedAt = command.getLaunchedYear().map(Year::of).orElse(null);
        final var categories = toIdentifiers(command.categories(), CategoryID::from);
        final var genres = toIdentifiers(command.genres(), GenreID::from);
        final var members = toIdentifiers(command.members(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        final var newVideo = Video.newVideo(
                command.title(),
                command.description(),
                launchedAt,
                command.duration(),
                rating,
                command.opened(),
                command.published(),
                categories,
                genres,
                members
        );

        newVideo.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException(notification.getErrors());
        }

        return CreateVideoOutput.from(create(command, newVideo));
    }

    private Video create(final CreateVideoCommand command, final Video video) {
        final var id = video.getId();
        try {
            final var trailerMedia = command.getTrailer()
                    .map(it -> this.resourceGateway.storeAudioVideo(id, it))
                    .orElse(null);

            final var videoMedia = command.getVideo()
                    .map(it -> this.resourceGateway.storeAudioVideo(id, it))
                    .orElse(null);

            final var bannerMedia = command.getBanner()
                    .map(it -> this.resourceGateway.storeImage(id, it))
                    .orElse(null);

            final var thumbnailMedia = command.getThumbnail()
                    .map(it -> this.resourceGateway.storeImage(id, it))
                    .orElse(null);

            final var thumbnailHalfMedia = command.getThumbnailHalf()
                    .map(it -> this.resourceGateway.storeImage(id, it))
                    .orElse(null);

            video.setVideo(videoMedia);
            video.setTrailer(trailerMedia);
            video.setBanner(bannerMedia);
            video.setThumbnail(thumbnailMedia);
            video.setThumbnailHalf(thumbnailHalfMedia);
            return this.videoGateway.create(video);
        } catch (final Throwable throwable) {
            this.resourceGateway.clear(id);
            throw InternalErrorException.with("An error on create video was thrown [id: %s]"
                    .formatted(id.getValue()), throwable);
        }
    }

    private ValidationHandler validateCategories(final Set<CategoryID> categories) {
        return validateExistingIdentifiers("categories", categories, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> genres) {
        return validateExistingIdentifiers("genres", genres, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> members) {
        return validateExistingIdentifiers("cast members", members, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateExistingIdentifiers(
            final String aggregateName,
            final Set<T> identifiers,
            final Function<Iterable<T>, List<T>> existsByID) {

        final var notification = Notification.create();
        if (identifiers == null || identifiers.isEmpty()) {
            return notification;
        }

        final var notFound = new ArrayList<>(identifiers);
        final var found = existsByID.apply(identifiers);

        notFound.removeAll(found);
        if (notFound.isEmpty()) {
            return notification;
        }

        final var missingIds = notFound
                .stream()
                .map(Identifier::getValue)
                .collect(Collectors.joining(", "));

        notification.append(new Error("Some %s could not be found: %s".formatted(aggregateName, missingIds)));
        return notification;
    }

    private <T> Set<T> toIdentifiers(
            final Set<String> values,
            final Function<String, T> mapper) {
        return values.stream().map(mapper).collect(Collectors.toSet());
    }

}
