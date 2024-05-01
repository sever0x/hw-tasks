package com.sever0x.block2.controller;

import com.sever0x.block2.model.request.ArtistRequest;
import com.sever0x.block2.model.response.ArtistResponse;
import com.sever0x.block2.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ArtistResponse createArtist(@RequestBody @Valid ArtistRequest request) {
        return artistService.createArtist(request);
    }
}
