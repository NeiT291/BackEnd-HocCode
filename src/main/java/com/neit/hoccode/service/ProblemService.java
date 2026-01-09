package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.ProblemRequest;
import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.dto.response.ProblemResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.Problem;
import com.neit.hoccode.entity.ProblemTestcase;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.ProblemMapper;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.repository.ContestRepository;
import com.neit.hoccode.repository.CourseModuleRepository;
import com.neit.hoccode.repository.ProblemRepository;
import com.neit.hoccode.repository.UserRepository;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;
    private final UserRepository userRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final CourseModuleRepository courseModuleRepository;
    private final ContestRepository contestRepository;

    public ProblemService(ProblemRepository problemRepository, ProblemMapper problemMapper, UserRepository userRepository, ResultPaginationMapper resultPaginationMapper, CourseModuleRepository courseModuleRepository, ContestRepository contestRepository) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
        this.userRepository = userRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.courseModuleRepository = courseModuleRepository;
        this.contestRepository = contestRepository;
    }

    public ProblemResponse addProblem(ProblemRequest request){
        Problem problem = new Problem();
        problemMapper.toProblem(request);
        MergeObject.mergeIgnoreNull(problemMapper.toProblem(request), problem);
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(request.getModuleId() != null){
            problem.setModule(courseModuleRepository.findById(request.getModuleId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_MODULE_NOT_FOUND)));
        }
        if(request.getContestId() != null){
            problem.setContest(contestRepository.findById(request.getContestId()).orElseThrow(()-> new AppException(ErrorCode.COURSE_MODULE_NOT_FOUND)));
        }
        problem.setCreatedAt(LocalDateTime.now());
        problem.setCreatedBy(user);
        for (ProblemTestcase testcase : problem.getTestcases()){
            testcase.setProblem(problem);
        }
        return problemMapper.toProblemResponse(problemRepository.save(problem));
    }
    public ProblemResponse modifyProblem(ProblemRequest request){
        Problem problem = problemRepository.findById(request.getId()).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND));

        MergeObject.mergeIgnoreNull(problemMapper.toProblem(request), problem);

        for (ProblemTestcase testcase: request.getTestcases()){
            testcase.setProblem(problem);
        }

        return problemMapper.toProblemResponse(problemRepository.save(problem));
    }
    public ProblemResponse getById(Integer id) {
        return problemMapper.toProblemResponse(problemRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND)));
    }
    public ProblemResponse getBySlug(String slug) {
        return problemMapper.toProblemResponse(problemRepository.findBySlug(slug).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND)));
    }
    public ResultPaginationResponse getAll(Optional<Integer> page, Optional<Integer> pageSize, String difficulty){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<ProblemResponse> problemPage;
        if (difficulty == null){
            problemPage = problemRepository.findAll(pageable).map(problemMapper::toProblemResponse);
        }else{
            problemPage = problemRepository.findAllByDifficulty(pageable, difficulty).map(problemMapper::toProblemResponse);
        }
        return resultPaginationMapper.toResultPaginationResponse(problemPage);
    }
    public ResultPaginationResponse getByTitle(String title, Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        String[] words = title.split(" ");
        title = String.join(" ", words);

        Page<ProblemResponse> companyPage = problemRepository.findByTitleIgnoreCaseContaining(title, pageable).map(problemMapper::toProblemResponse);

        return resultPaginationMapper.toResultPaginationResponse(companyPage);
    }

    public ResultPaginationResponse getCreated(Optional<Integer> page, Optional<Integer> pageSize) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<ProblemResponse> problemPage = problemRepository.findByCreatedByIdAndIsTheory(user.getId(),false, pageable).map(problemMapper::toProblemResponse);
        return resultPaginationMapper.toResultPaginationResponse(problemPage);
    }
}
