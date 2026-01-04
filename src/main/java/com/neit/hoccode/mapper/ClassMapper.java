package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.request.ClassRequest;
import com.neit.hoccode.dto.response.ClassResponse;
import com.neit.hoccode.entity.Class;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ClassMapper {
    Class toClass(ClassRequest request);
    ClassResponse toClassResponse(Class classRoom);
}
