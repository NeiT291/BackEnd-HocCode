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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final ClassRepository classRepository;
    private final MinioService minioService;

    public CourseService(CourseMapper courseMapper, CourseRepository courseRepository, UserRepository userRepository, ResultPaginationMapper resultPaginationMapper, CourseEnrollmentRepository courseEnrollmentRepository, ClassRepository classRepository, MinioService minioService) {
        this.courseMapper = courseMapper;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.classRepository = classRepository;
        this.minioService = minioService;
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
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseIdAndUserId(course.getId(), user.getId());
        if (courseEnrollment != null){
            return courseEnrollment;
        }
        return courseEnrollmentRepository.save(CourseEnrollment.builder().user(user).course(course).enrolledAt(LocalDateTime.now()).build());
    }
    public void outCourse(int courseId){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseIdAndUserId(courseId, user.getId());
        if(courseEnrollment != null ){
            courseEnrollmentRepository.delete(courseEnrollment);
        }
    }
    public ResultPaginationResponse getAll(Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<CourseResponse> coursePage = courseRepository.findAll(pageable).map(courseMapper::toCourseResponse);
        return resultPaginationMapper.toResultPaginationResponse(coursePage);
    }
    public ResultPaginationResponse getByTitle(String title, Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        String[] words = title.split(" ");
        title = String.join(" ", words);

        Page<CourseResponse> coursePage = courseRepository.findByTitleIgnoreCaseContaining(title, pageable).map(courseMapper::toCourseResponse);

        return resultPaginationMapper.toResultPaginationResponse(coursePage);
    }

    public Void setThumbnail(Integer courseId, MultipartFile thumbnail) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        try{
            String objectName = minioService.uploadImage(thumbnail);
            String url = "http://localhost:9000/hoccode/" + objectName;
            course.setThumbnailUrl(url);
            courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public CourseResponse getCourseById(Integer id) {
        return courseMapper.toCourseResponse(courseRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND)));
    }
    public CourseEnrollment isJoin(Integer id){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseEnrollmentRepository.findByCourseIdAndUserId(course.getId(), user.getId());
    }
    public ResultPaginationResponse getCourseCreated(Optional<Integer> page, Optional<Integer> pageSize){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<CourseResponse> coursePage = courseRepository.findByOwnerId(user.getId(), pageable).map(courseMapper::toCourseResponse);
        return resultPaginationMapper.toResultPaginationResponse(coursePage);
    }
    public ResultPaginationResponse getCourseJoined(Optional<Integer> page, Optional<Integer> pageSize){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<CourseEnrollment> courseEnrollments = courseEnrollmentRepository.findByUserId(user.getId(), pageable);
        Page<CourseResponse> coursePage = courseEnrollments.map(CourseEnrollment::getCourse).map(courseMapper::toCourseResponse);
        return resultPaginationMapper.toResultPaginationResponse(coursePage);
    }
}
