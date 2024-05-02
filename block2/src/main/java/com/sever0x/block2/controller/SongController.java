package com.sever0x.block2.controller;

import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.GetSongsResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
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

    @GetMapping("/{id}")
    public SongResponse getSongDetails(@PathVariable long id) {
        return songService.getSongById(id);
    }

    @PutMapping("/{id}")
    public void updateSong(@PathVariable long id, @RequestBody @Valid SongRequest request) {
        songService.updateSongById(id, request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteSong(@PathVariable long id) {
        return songService.deleteSongById(id);
    }

    @PostMapping("/_list")
    public GetSongsResponse getSongs(@RequestBody @Valid GetSongsRequest request) {
        return songService.getSongs(request);
    }
}
