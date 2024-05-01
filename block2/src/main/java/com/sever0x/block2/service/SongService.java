package com.sever0x.block2.service;

import com.sever0x.block2.model.request.SongRequest;
import com.sever0x.block2.model.response.SongResponse;

public interface SongService {

    SongResponse createSong(SongRequest request);

    SongResponse getSongById(long id);

    SongResponse updateSongById(SongRequest request);
}
