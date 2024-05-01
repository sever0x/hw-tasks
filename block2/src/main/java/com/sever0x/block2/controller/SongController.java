package com.sever0x.block2.controller;

import com.sever0x.block2.model.request.SongRequest;
import com.sever0x.block2.model.response.SongResponse;
import com.sever0x.block2.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    public SongResponse createSong(@RequestBody @Valid SongRequest request) {
        return songService.createSong(request);
    }
}
