package com.sever0x.block2.service.impl;

import com.sever0x.block2.mapper.SongMapper;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.GetSongsResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.model.entity.Song;
import com.sever0x.block2.repository.ArtistRepository;
import com.sever0x.block2.repository.SongRepository;
import com.sever0x.block2.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        song.setArtist(getArtistOrThrow(request.artistId()));
        return songMapper.entityToResponse(songRepository.save(song));
    }

    @Override
    public SongResponse getSongById(long id) {
        return songMapper.entityToResponse(getSongOrThrow(id));
    }

    @Override
    public void updateSongById(long id, SongRequest request) {
        Song updatableSong = getSongOrThrow(id);
        updateSongFromRequest(updatableSong, request);
        songRepository.save(updatableSong);
    }

    @Override
    public boolean deleteSongById(long id) {
        if (!songRepository.existsById(id)) {
            throw getResponseStatusExceptionNotFound("Song with ID ", id);
        }
        songRepository.deleteById(id);
        return true;
    }

    @Override
    public GetSongsResponse getSongs(GetSongsRequest request) {
        Page<Song> songs = songRepository.findAll(getSongsPageable(request));
        GetSongsResponse response = GetSongsResponse.builder()
                .list(songs.map(songMapper::toShortResponse).stream().toList())
                .totalPages(songs.getTotalPages())
                .build();
        return response;
    }

    private Artist getArtistOrThrow(long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> getResponseStatusExceptionNotFound("Artist with ID ", artistId));
    }

    private Song getSongOrThrow(long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> getResponseStatusExceptionNotFound("Song with ID ", id));
    }

    private void updateSongFromRequest(Song song, SongRequest request) {
        song.setAlbum(request.album());
        song.setTitle(request.title());
        song.setArtist(getArtistOrThrow(request.artistId()));
        song.setGenres(request.genres());
        song.setDuration(request.duration());
        song.setReleaseYear(request.releaseYear());
    }

    private Pageable getSongsPageable(GetSongsRequest request) {
        return PageRequest.of(request.page(), request.size());
    }

    private static ResponseStatusException getResponseStatusExceptionNotFound(String message, long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message + id + " doesn't exist");
    }
}
