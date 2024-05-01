package com.sever0x.block2.mapper;

import com.sever0x.block2.model.entity.Song;
import com.sever0x.block2.model.request.SongRequest;
import com.sever0x.block2.model.response.SongResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SongMapper {

    Song requestToEntity(SongRequest request);

    SongResponse entityToResponse(Song song);
}
