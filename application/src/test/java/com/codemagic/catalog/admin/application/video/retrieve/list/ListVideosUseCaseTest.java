package com.codemagic.catalog.admin.application.video.retrieve.list;

import com.codemagic.catalog.admin.Fixture;
import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.pagination.Pagination;
import com.codemagic.catalog.admin.domain.video.Video;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListVideosUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    void givenAValidQuery_whenCallsListVideos_thenShouldReturnPaginatedListOfVideos() {
        // given
        final var videos = List.of(Fixture.Videos.video());
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedCount = 1;
        final var expectedItemsCount = 1;
        final var expectedTerms = "abc";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedItems = videos.stream().map(VideoListOutput::from).toList();

        final var expectedQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedCount, videos);
        when(videoGateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(expectedQuery));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedCount, actualOutput.total());
        assertEquals(expectedItemsCount, actualOutput.items().size());
        assertEquals(expectedItemsCount, actualOutput.items().size());
        assertEquals(expectedItems, actualOutput.items());

        verify(videoGateway, times(1)).findAll(eq(expectedQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListVideosAndDataSourceIsEmpty_thenShouldReturnPaginated() {
        // given
        final var videos = List.<Video>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedCount = 0;
        final var expectedItemsCount = 0;
        final var expectedTerms = "abc";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedItems = List.<VideoListOutput>of();

        final var expectedQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedCount, videos);
        when(videoGateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = assertDoesNotThrow(() -> this.useCase.execute(expectedQuery));

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedCount, actualOutput.total());
        assertEquals(expectedItemsCount, actualOutput.items().size());
        assertEquals(expectedItemsCount, actualOutput.items().size());
        assertEquals(expectedItems, actualOutput.items());

        verify(videoGateway, times(1)).findAll(eq(expectedQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListVideosAndGatewayThrowsAnError_thenShouldReturnAnError() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "abc";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var expectedQuery = new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        doThrow(new IllegalStateException("Gateway error")).when(videoGateway).findAll(any());

        // when
        final var actualException = assertThrows(IllegalStateException.class,
                () -> this.useCase.execute(expectedQuery));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(videoGateway, times(1)).findAll(eq(expectedQuery));
    }

}
