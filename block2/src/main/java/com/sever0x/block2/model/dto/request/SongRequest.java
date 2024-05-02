package com.sever0x.block2.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record SongRequest(
        @NotBlank(message = "Title is mandatory")
        String title,
        @Positive
        long artistId,
        @NotBlank(message = "Album is mandatory")
        String album,
        @NotBlank(message = "Genres is mandatory")
        String genres,
        @Positive
        @Min(10) // min duration of song - 10s
        int duration,
        @Positive
        int releaseYear
) {
}
