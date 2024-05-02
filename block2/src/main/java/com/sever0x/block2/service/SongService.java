package com.sever0x.block2.service;

import com.sever0x.block2.model.dto.request.GenerateReportSongsRequest;
import com.sever0x.block2.model.dto.request.GetSongsRequest;
import com.sever0x.block2.model.dto.request.SongRequest;
import com.sever0x.block2.model.dto.response.GenerateReportSongsResponse;
import com.sever0x.block2.model.dto.response.GetSongsResponse;
import com.sever0x.block2.model.dto.response.SongResponse;

public interface SongService {

    SongResponse createSong(SongRequest request);

    SongResponse getSongById(long id);

    void updateSongById(long id, SongRequest request);

    boolean deleteSongById(long id);

    GetSongsResponse getSongs(GetSongsRequest request);

    GenerateReportSongsResponse generateReportSongs(GenerateReportSongsRequest request);
}
