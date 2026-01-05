package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.ClassRequest;
import com.neit.hoccode.dto.response.ClassResponse;
import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.Class;
import com.neit.hoccode.entity.ClassEnrollment;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.ClassMapper;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.repository.ClassEnrollmentRepository;
import com.neit.hoccode.repository.ClassRepository;
import com.neit.hoccode.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ClassService {
    private final ClassRepository classRepository;
    private final ClassMapper classMapper;
    private final UserRepository userRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final ClassEnrollmentRepository classEnrollmentRepository;

    public ClassService(ClassRepository classRepository, ClassMapper classMapper, UserRepository userRepository, ResultPaginationMapper resultPaginationMapper, ClassEnrollmentRepository classEnrollmentRepository) {
        this.classRepository = classRepository;
        this.classMapper = classMapper;
        this.userRepository = userRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.classEnrollmentRepository = classEnrollmentRepository;
    }

    public ClassResponse add(ClassRequest request){
        if(classRepository.findByCode(request.getCode()) != null){
            throw new AppException(ErrorCode.CLASS_CODE_EXIST);
        }
        Class classRoom = classMapper.toClass(request);
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        classRoom.setOwner(user);
        classRoom.setCreatedAt(LocalDateTime.now());
        return classMapper.toClassResponse(classRepository.save(classRoom));
    }
    public ClassResponse modify(ClassRequest request){
        Class classRoom = classRepository.findById(request.getId()).orElseThrow(()->new AppException(ErrorCode.CLASS_NOT_FOUND));
        Class classByCode = classRepository.findByCode(request.getCode());
        if(classByCode != null && !classByCode.equals(classRoom)){
            throw new AppException(ErrorCode.CLASS_CODE_EXIST);
        }
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!classRoom.getOwner().equals(user)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        classRoom.setTitle(request.getTitle());
        classRoom.setCode(request.getCode());
        classRoom.setDescription(request.getDescription());

        return classMapper.toClassResponse(classRepository.save(classRoom));
    }

    public Boolean enroll(Integer classId) {
        Class classRoom = classRepository.findById(classId).orElseThrow(()->new AppException(ErrorCode.CLASS_NOT_FOUND));
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(classEnrollmentRepository.findByUserAndClassRoom(user, classRoom)!=null){
            return true;
        }
        classEnrollmentRepository.save(ClassEnrollment.builder().user(user).classRoom(classRoom).enrolledAt(LocalDateTime.now()).build());
        return true;
    }

    public void outClass(Integer classId) {
        Class classRoom = classRepository.getReferenceById(classId);
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        ClassEnrollment classEnrollment = classEnrollmentRepository.findByUserAndClassRoom(user, classRoom);
        if(classEnrollment != null){
            classEnrollmentRepository.delete(classEnrollment);
        }
    }
    public ResultPaginationResponse getAll(Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<ClassResponse> classPage = classRepository.findAll(pageable).map(classMapper::toClassResponse);
        return resultPaginationMapper.toResultPaginationResponse(classPage);
    }
    public ResultPaginationResponse getByTitle(String title, Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        String[] words = title.split(" ");
        title = String.join(" ", words);

        Page<ClassResponse> companyPage = classRepository.findByTitleIgnoreCaseContaining(title, pageable).map(classMapper::toClassResponse);

        return resultPaginationMapper.toResultPaginationResponse(companyPage);
    }
}
