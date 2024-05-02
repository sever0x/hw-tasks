package com.sever0x.block2.controller;

import com.sever0x.block2.exception.InvalidJsonException;
import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.GenerateReportSongsResponse;
import com.sever0x.block2.model.dto.response.GetSongsResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
import com.sever0x.block2.model.dto.response.UploadResponse;
import com.sever0x.block2.service.SongService;
import com.sever0x.block2.service.impl.SongServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/_report")
    public ResponseEntity<Resource> generateReportSongs(@RequestBody @Valid GenerateReportSongsRequest request) {
        GenerateReportSongsResponse response = songService.generateReportSongs(request);
        InputStreamResource resource = new InputStreamResource(response.report());

        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(response.report().available())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + response.name() + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    public UploadResponse uploadSongs(@RequestParam("file") MultipartFile file) {
        return songService.importSongsFromFile(file); //fixme
    }
}
