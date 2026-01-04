package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.CourseRequest;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.*;
import com.neit.hoccode.entity.Class;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.CourseMapper;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.repository.ClassRepository;
import com.neit.hoccode.repository.CourseEnrollmentRepository;
import com.neit.hoccode.repository.CourseRepository;
import com.neit.hoccode.repository.UserRepository;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final ClassRepository classRepository;

    public CourseService(CourseMapper courseMapper, CourseRepository courseRepository, UserRepository userRepository, ResultPaginationMapper resultPaginationMapper, CourseEnrollmentRepository courseEnrollmentRepository, ClassRepository classRepository) {
        this.courseMapper = courseMapper;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.classRepository = classRepository;
    }

    public CourseResponse addCourse(CourseRequest request){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseMapper.toCourse(request);
        course.setCreatedAt(LocalDateTime.now());
        course.setOwner(user);
        if(request.getIsPublic()==null){
            course.setIsPublic(true);
        }
        if(request.getClassId()!=null){
            Class classRoom = classRepository.findById(request.getClassId()).orElseThrow(()->new AppException(ErrorCode.CLASS_NOT_FOUND));
            course.setClassRoom(classRoom);
        }
        if(request.getModules()==null){
            return courseMapper.toCourseResponse(courseRepository.save(course));
        }
        List<CourseModule> modules = request.getModules();
        for(CourseModule module : modules){
            module.setCourse(course);
            module.setCreatedAt(LocalDateTime.now());

            List<Lesson> lessons = module.getLessons();
            for(Lesson lesson: lessons){
                lesson.setModule(module);
                lesson.setCreatedAt(LocalDateTime.now());
            }

            module.setLessons(lessons);
        }
        request.setModules(modules);

        return courseMapper.toCourseResponse(courseRepository.save(course));
    }
    public CourseResponse modifyCourse(CourseRequest request) {
        Course course = courseRepository.findById(request.getId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if(courseRepository.getBySlug(request.getSlug()) != null && !courseRepository.getBySlug(request.getSlug()).equals(course)){
            throw new AppException(ErrorCode.COURSE_SLUG_EXISTED);
        };

        course.setTitle(request.getTitle());
        course.setSlug(request.getSlug());
        course.setIsPublic(request.getIsPublic());
        course.setDescription(request.getDescription());
        course.setUpdatedAt(LocalDateTime.now());


        return courseMapper.toCourseResponse(courseRepository.save(course));
    }
    public CourseResponse getCourseBySlug(String slug){
        return courseMapper.toCourseResponse(courseRepository.findBySlug(slug).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND)));
    }
    public CourseEnrollment enRoll(int courseId){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository.findById(courseId).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseEnrollmentRepository.save(CourseEnrollment.builder().user(user).course(course).enrolledAt(LocalDateTime.now()).build());
    }
    public void outCourse(int courseId){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseId(courseId);
        if(courseEnrollment != null && courseEnrollment.getUser() == user){
            courseEnrollmentRepository.delete(courseEnrollment);
        }
    }
    public ResultPaginationResponse getAll(Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<CourseResponse> coursePage = courseRepository.findAll(pageable).map(courseMapper::toCourseResponse);
        return resultPaginationMapper.toResultPaginationResponse(coursePage);
    }
}
