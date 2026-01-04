package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.request.ProblemRequest;
import com.neit.hoccode.dto.response.ProblemResponse;
import com.neit.hoccode.entity.Problem;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ProblemMapper {
    Problem toProblem(ProblemRequest problemRequest);
    ProblemResponse toProblemResponse(Problem problem);
}
