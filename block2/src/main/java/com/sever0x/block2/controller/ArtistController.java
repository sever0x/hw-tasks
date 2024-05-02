package com.sever0x.block2.controller;

import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ArtistResponse createArtist(@RequestBody @Valid ArtistRequest request) {
        return artistService.createArtist(request);
    }

    @GetMapping
    public List<ArtistResponse> getArtists() {
        return artistService.getArtists();
    }

    @PutMapping("/{id}")
    public void updateArtist(@PathVariable long id, @RequestBody @Valid ArtistRequest request) {
        artistService.updateArtistById(id, request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteArtist(@PathVariable long id) {
        return artistService.deleteArtistById(id);
    }
}
