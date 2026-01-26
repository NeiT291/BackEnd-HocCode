package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.CourseModuleRequest;
import com.neit.hoccode.dto.response.CourseModuleResponse;
import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.CourseModule;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.CourseModuleMapper;
import com.neit.hoccode.repository.CourseModuleRepository;
import com.neit.hoccode.repository.CourseRepository;
import com.neit.hoccode.repository.UserRepository;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CourseModuleService {
    private final CourseModuleRepository courseModuleRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleMapper courseModuleMapper;
    private final UserRepository userRepository;

    public CourseModuleService(CourseModuleRepository courseModuleRepository, CourseRepository courseRepository, CourseModuleMapper courseModuleMapper, UserRepository userRepository) {
        this.courseModuleRepository = courseModuleRepository;
        this.courseRepository = courseRepository;
        this.courseModuleMapper = courseModuleMapper;
        this.userRepository = userRepository;
    }

    public CourseModuleResponse addCourseModule(CourseModuleRequest request){
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(course.getOwner() != user && !Objects.equals(user.getRole().getName(), "ADMIN")){
            throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
        }
        return courseModuleMapper.toCourseModuleResponse(courseModuleRepository.save(CourseModule.builder()
                .title(request.getTitle())
                .course(course)
                .position(request.getPosition())
                .createdAt(LocalDateTime.now())
                .build()));
    }

    public CourseModuleResponse modifyCourseModule(CourseModuleRequest request) {
        CourseModule courseModule = courseModuleRepository.findById(request.getId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(courseModule.getCourse().getId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(course.getOwner() != user && !Objects.equals(user.getRole().getName(), "ADMIN")){
            throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
        }
        MergeObject.mergeIgnoreNull(courseModuleMapper.toCourseModule(request), courseModule);
        return courseModuleMapper.toCourseModuleResponse(courseModuleRepository.save(courseModule));
    }

    public List<CourseModuleResponse> getByCourseId(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseModuleRepository.findByCourse(course).stream().map(courseModuleMapper::toCourseModuleResponse).toList();
    }
    public void deleteCourseModule(Integer id){
        if (courseModuleRepository.findById(id).isEmpty()){
            return;
        }
        CourseModule courseModule = courseModuleRepository.getReferenceById(id);
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(courseModule.getCourse().getOwner() != user && !Objects.equals(user.getRole().getName(), "ADMIN")){
            throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
        }
        courseModule.setIsActive(false);
        courseModuleRepository.save(courseModule);
    }
}
