package com.sever0x.block2.model.dto.response;

public record ShortSongResponse(
        String title,
        String artistName,
        String album,
        int duration
) {
}
