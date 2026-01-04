package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.CourseModuleRequest;
import com.neit.hoccode.dto.response.CourseModuleResponse;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.CourseModuleMapper;
import com.neit.hoccode.repository.CourseModuleRepository;
import com.neit.hoccode.repository.CourseRepository;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseModuleService {
    private final CourseModuleRepository courseModuleRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleMapper courseModuleMapper;

    public CourseModuleService(CourseModuleRepository courseModuleRepository, CourseRepository courseRepository, CourseModuleMapper courseModuleMapper) {
        this.courseModuleRepository = courseModuleRepository;
        this.courseRepository = courseRepository;
        this.courseModuleMapper = courseModuleMapper;
    }

    public CourseModuleResponse addCourseModule(CourseModuleRequest request){
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        return courseModuleMapper.toCourseModuleResponse(courseModuleRepository.save(CourseModule.builder()
                .title(request.getTitle())
                .course(course)
                .position(request.getPosition())
                .lessons(request.getLessons())
                .createdAt(LocalDateTime.now())
                .build()));
    }

    public CourseModuleResponse modifyCourseModule(CourseModuleRequest request) {
        CourseModule courseModule = courseModuleRepository.findById(request.getId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        MergeObject.mergeIgnoreNull(courseModuleMapper.toCourseModule(request), courseModule);
        return courseModuleMapper.toCourseModuleResponse(courseModuleRepository.save(courseModule));
    }

    public List<CourseModuleResponse> getByCourseId(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseModuleRepository.findByCourse(course).stream().map(courseModuleMapper::toCourseModuleResponse).toList();
    }
}
