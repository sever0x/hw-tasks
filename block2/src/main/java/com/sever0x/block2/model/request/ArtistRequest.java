package com.sever0x.block2.model.request;

import jakarta.validation.constraints.NotBlank;

public record ArtistRequest(
        @NotBlank
        String name,
        @NotBlank
        String country
) {
}
