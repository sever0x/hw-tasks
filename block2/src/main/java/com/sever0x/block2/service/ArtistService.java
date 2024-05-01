package com.sever0x.block2.service;

import com.sever0x.block2.model.request.ArtistRequest;
import com.sever0x.block2.model.response.ArtistResponse;

public interface ArtistService {

    ArtistResponse createArtist(ArtistRequest request);
}
