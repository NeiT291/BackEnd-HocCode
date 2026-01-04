package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.request.CourseRequest;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.entity.Course;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRequest request);
    CourseResponse toCourseResponse(Course course);
    Course copy(Course course);
}
