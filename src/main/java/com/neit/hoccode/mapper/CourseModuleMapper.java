package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.request.CourseModuleRequest;
import com.neit.hoccode.dto.response.CourseModuleResponse;
import com.neit.hoccode.entity.CourseModule;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CourseModuleMapper {
    CourseModule toCourseModule(CourseModuleRequest courseModuleRequest);
    CourseModuleResponse toCourseModuleResponse(CourseModule courseModule);
}
