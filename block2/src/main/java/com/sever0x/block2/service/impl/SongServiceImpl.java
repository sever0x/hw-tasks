package com.sever0x.block2.service.impl;

import com.sever0x.block2.mapper.SongMapper;
import com.sever0x.block2.model.entity.Song;
import com.sever0x.block2.model.request.SongRequest;
import com.sever0x.block2.model.response.SongResponse;
import com.sever0x.block2.repository.ArtistRepository;
import com.sever0x.block2.repository.SongRepository;
import com.sever0x.block2.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongMapper songMapper;

    private final SongRepository songRepository;

    private final ArtistRepository artistRepository;

    @Override
    public SongResponse createSong(SongRequest request) {
        Song song = songMapper.requestToEntity(request);
        song.setArtist(artistRepository.findById(request.artistId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Artist with ID " + request.artistId() + " doesn't exist"))
        );

        return songMapper.entityToResponse(songRepository.save(song));
    }

    @Override
    public SongResponse getSongById(long id) {
        return null;
    }

    @Override
    public SongResponse updateSongById(SongRequest request) {
        return null;
    }
}
