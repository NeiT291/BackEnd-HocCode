package com.neit.hoccode.mapper;


import com.neit.hoccode.dto.request.ProblemTestcaseRequest;
import com.neit.hoccode.entity.ProblemTestcase;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ProblemTestcaseMapper {
    ProblemTestcase toProblemTestcase(ProblemTestcaseRequest problemTestcaseRequest);
}
