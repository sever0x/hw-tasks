package com.sever0x.block2.service.impl;

import com.sever0x.block2.exception.ArtistNotFoundException;
import com.sever0x.block2.exception.InvalidJsonException;
import com.sever0x.block2.mapper.SongMapper;
import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.GenerateReportSongsResponse;
import com.sever0x.block2.model.dto.response.GetSongsResponse;
import com.sever0x.block2.model.dto.response.SongResponse;
import com.sever0x.block2.model.dto.response.UploadResponse;
import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.model.entity.Song;
import com.sever0x.block2.parser.json.JsonPlaylistParser;
import com.sever0x.block2.parser.json.ParsedSong;
import com.sever0x.block2.parser.xslx.ExcelWriter;
import com.sever0x.block2.repository.ArtistRepository;
import com.sever0x.block2.repository.SongRepository;
import com.sever0x.block2.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private static final String EXCEL_REPORT_NAME = "report_by_%s.xlsx";

    private final ExcelWriter writer;

    private final SongMapper songMapper;

    private final SongRepository songRepository;

    private final ArtistRepository artistRepository;

    private final JsonPlaylistParser jsonPlaylistParser;

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
        Page<Song> songs = songRepository.findAll(getSongsPageable(request), request.artistId(), request.album());
        return GetSongsResponse.builder()
                .list(songs.map(songMapper::toShortResponse).stream().toList())
                .totalPages(songs.getTotalPages())
                .build();
    }

    @Override
    public GenerateReportSongsResponse generateReportSongs(GenerateReportSongsRequest request) {
        List<Song> songs = songRepository.findAll(
                PageRequest.of(0, Integer.MAX_VALUE), request.artistId(), request.album()
        ).getContent();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.createExcelReport(songs, out);

        String fileName = formatExcelReportName(request.artistId(), request.album());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(out.toByteArray().length);

        return new GenerateReportSongsResponse(fileName, new ByteArrayInputStream(out.toByteArray()));
    }

    @Override
    public UploadResponse importSongsFromFile(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        try {
            List<ParsedSong> parsedSongs = jsonPlaylistParser.parseSongsFromFile(file.getInputStream());
            List<String> invalidRecords = new ArrayList<>();

            for (ParsedSong parsedSong : parsedSongs) {
                try {
                    Song song = createSongFromParsedSong(parsedSong);
                    songRepository.save(song);
                    successCount++;
                } catch (ArtistNotFoundException e) {
                    invalidRecords.add(parsedSong.title());
                }
            }

            if (!invalidRecords.isEmpty()) {
                failureCount++;
                throw new InvalidJsonException("Some records were invalid", invalidRecords);
            }

            return new UploadResponse(successCount, failureCount);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private Song createSongFromParsedSong(ParsedSong parsedSong) {
        Artist artist = artistRepository.findByNameAndCountry(parsedSong.artistName(), parsedSong.artistCountry())
                .orElseGet(() -> {
                    Artist newArtist = Artist.builder()
                            .country(parsedSong.artistCountry())
                            .name(parsedSong.artistName())
                            .build();
                    return artistRepository.save(newArtist);
                });

        return Song.builder()
                .title(parsedSong.title())
                .artist(artist)
                .album(parsedSong.album())
                .genres(parsedSong.genres())
                .duration(parsedSong.duration())
                .releaseYear(parsedSong.releaseYear())
                .build();
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

    private String formatExcelReportName(Long artistId, String album) {
        if (artistId != null && album != null) {
            return String.format(EXCEL_REPORT_NAME, "artistId_and_album");
        } else if (artistId != null) {
            return String.format(EXCEL_REPORT_NAME, "artistId");
        } else {
            return String.format(EXCEL_REPORT_NAME, "album");
        }
    }
}
