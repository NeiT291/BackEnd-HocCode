package com.neit.hoccode.mapper;


import com.neit.hoccode.dto.request.ContestRequest;
import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.entity.Contest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ContestMapper {
    Contest toContest(ContestRequest request);
    ContestResponse toContestResponse(Contest contest);
}
