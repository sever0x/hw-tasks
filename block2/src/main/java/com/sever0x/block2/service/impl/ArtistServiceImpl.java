package com.sever0x.block2.service.impl;

import com.sever0x.block2.mapper.ArtistMapper;
import com.sever0x.block2.model.dto.request.ArtistRequest;
import com.sever0x.block2.model.dto.response.ArtistResponse;
import com.sever0x.block2.repository.ArtistRepository;
import com.sever0x.block2.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistMapper artistMapper;

    private final ArtistRepository artistRepository;

    @Override
    public ArtistResponse createArtist(ArtistRequest request) {
        return artistMapper.entityToResponse(
                artistRepository.save(
                        artistMapper.requestToEntity(request)
                )
        );
    }
}
