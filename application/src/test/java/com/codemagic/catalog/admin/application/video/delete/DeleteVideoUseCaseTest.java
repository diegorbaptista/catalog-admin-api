package com.codemagic.catalog.admin.application.video.delete;

import com.codemagic.catalog.admin.application.UseCaseTest;
import com.codemagic.catalog.admin.domain.exceptions.InternalErrorException;
import com.codemagic.catalog.admin.domain.video.MediaResourceGateway;
import com.codemagic.catalog.admin.domain.video.VideoGateway;
import com.codemagic.catalog.admin.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Test
    void givenAValidVideoId_whenCallDeleteVideo_thenShouldDeleteAVideo() {
        // given
        final var videoId = VideoID.unique();

        doNothing().when(videoGateway).deleteById(any());
        doNothing().when(mediaResourceGateway).clear(any());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(videoId.getValue()));

        //then
        verify(videoGateway, times(1)).deleteById(eq(videoId));
        verify(mediaResourceGateway, times(1)).clear(eq(videoId));
    }

    @Test
    void givenAInvalidVideoId_whenCallDeleteVideo_thenShouldBeOk() {
        // given
        final var videoId = VideoID.from("1234");

        doNothing().when(videoGateway).deleteById(any());
        doNothing().when(mediaResourceGateway).clear(any());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(videoId.getValue()));

        //then
        verify(videoGateway, times(1)).deleteById(eq(videoId));
        verify(mediaResourceGateway, times(1)).clear(eq(videoId));
    }

    @Test
    void givenAInvalidVideoId_whenGatewayThrowsAnError_thenReturnAHandledErrorMessage() {
        // given
        final var videoId = VideoID.unique();
        final var expectedErrorMessage = "Internal server error";

        doThrow(new InternalErrorException(expectedErrorMessage, new RuntimeException(expectedErrorMessage)))
                .when(videoGateway)
                .deleteById(any());

        doNothing().when(mediaResourceGateway).clear(any());

        // when
        final var actualException = assertThrows(InternalErrorException.class, () -> this.useCase.execute(videoId.getValue()));

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(videoGateway, times(1)).deleteById(eq(videoId));
        verify(mediaResourceGateway, never()).clear(any());
    }

}
