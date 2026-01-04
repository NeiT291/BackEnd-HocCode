package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.request.CourseModuleRequest;
import com.neit.hoccode.dto.request.LessonRequest;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.Lesson;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LessonMapper {
    Lesson toLesson(LessonRequest request);
}
