package com.sever0x.block2.mapper;

import com.sever0x.block2.model.entity.Artist;
import com.sever0x.block2.model.request.ArtistRequest;
import com.sever0x.block2.model.response.ArtistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ArtistMapper {

    Artist requestToEntity(ArtistRequest request);

    ArtistResponse entityToResponse(Artist artist);
}
