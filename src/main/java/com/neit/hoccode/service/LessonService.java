package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.LessonRequest;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.Lesson;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.LessonMapper;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.repository.CourseModuleRepository;
import com.neit.hoccode.repository.LessonRepository;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final CourseModuleRepository courseModuleRepository;
    private final ResultPaginationMapper resultPaginationMapper;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, CourseModuleRepository courseModuleRepository, ResultPaginationMapper resultPaginationMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.courseModuleRepository = courseModuleRepository;
        this.resultPaginationMapper = resultPaginationMapper;
    }

    public Lesson addLesson(LessonRequest request){
        Lesson lesson = lessonMapper.toLesson(request);
        lesson.setCreatedAt(LocalDateTime.now());
        if( request.getModuleId() != null){
            lesson.setModule(courseModuleRepository.findById(request.getModuleId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_MODULE_NOT_FOUND)));
        }
        return lessonRepository.save(lesson);
    }
    public Lesson modifyLesson(LessonRequest request){
        if(request.getId() == null){
            throw new AppException(ErrorCode.LESSON_NOT_FOUND);
        }
        Lesson lesson = lessonRepository.findById(request.getId()).orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));
        MergeObject.mergeIgnoreNull(lessonMapper.toLesson(request), lesson);
        return lessonRepository.save(lesson);
    }

    public Lesson findById(int id){
        return lessonRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.LESSON_NOT_FOUND));
    }

    public ResultPaginationResponse getAll(Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<Lesson> lessonPage = lessonRepository.findAll(pageable);
        return resultPaginationMapper.toResultPaginationResponse(lessonPage);
    }
}
