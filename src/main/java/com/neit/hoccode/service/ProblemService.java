package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.ProblemRequest;
import com.neit.hoccode.dto.request.ProblemRunCodeRequest;
import com.neit.hoccode.dto.response.*;
import com.neit.hoccode.entity.*;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.ProblemMapper;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.repository.*;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;
    private final UserRepository userRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final CourseModuleRepository courseModuleRepository;
    private final ContestRepository contestRepository;
    private final RunCodeService runCodeService;
    private final SubmissionRepository submissionRepository;
    private final ProblemDoneRepository problemDoneRepository;
    private final CourseProgressRepository courseProgressRepository;
    public ProblemService(ProblemRepository problemRepository, ProblemMapper problemMapper, UserRepository userRepository, ResultPaginationMapper resultPaginationMapper, CourseModuleRepository courseModuleRepository, ContestRepository contestRepository, RunCodeService runCodeService, SubmissionRepository submissionRepository, ProblemDoneRepository problemDoneRepository, CourseProgressRepository courseProgressRepository) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
        this.userRepository = userRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.courseModuleRepository = courseModuleRepository;
        this.contestRepository = contestRepository;
        this.runCodeService = runCodeService;
        this.submissionRepository = submissionRepository;
        this.problemDoneRepository = problemDoneRepository;
        this.courseProgressRepository = courseProgressRepository;
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
        if(request.getMemoryLimitKb() > 256000){
            request.setMemoryLimitKb(256000);
        }
        if (request.getTimeLimitMs() > 15000){
            request.setTimeLimitMs(15000);
        }
        problem.setCreatedAt(LocalDateTime.now());
        problem.setCreatedBy(user);
        for (ProblemTestcase testcase : problem.getTestcases()){
            testcase.setProblem(problem);
        }
        if(problem.getCreatedBy() != user && !Objects.equals(user.getRole().getName(), "ADMIN")){
            throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
        }
        return problemMapper.toProblemResponse(problemRepository.save(problem));
    }
    public ProblemResponse modifyProblem(ProblemRequest request){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Problem problem = problemRepository.findById(request.getId()).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND));
        if(request.getMemoryLimitKb() != null && request.getMemoryLimitKb() > 256000){
            request.setMemoryLimitKb(256000);
        }
        if (request.getTimeLimitMs() != null && request.getTimeLimitMs() > 15000){
            request.setTimeLimitMs(15000);
        }
        MergeObject.mergeIgnoreNull(problemMapper.toProblem(request), problem);

        for (ProblemTestcase testcase: request.getTestcases()){
            if(testcase.getId() == -1){
                testcase.setId(null);
            }
            testcase.setProblem(problem);
        }
        if(problem.getCreatedBy() != user && !Objects.equals(user.getRole().getName(), "ADMIN")){
            throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
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
            problemPage = problemRepository.findAllByIsPublicAndIsActive(pageable, true, true).map(problemMapper::toProblemResponse);
        }else{
            problemPage = problemRepository.findAllByDifficultyAndIsPublicAndIsActive(pageable, difficulty,true, true).map(problemMapper::toProblemResponse);
        }
        return resultPaginationMapper.toResultPaginationResponse(problemPage);
    }
    public ResultPaginationResponse getByTitle(String title, Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        String[] words = title.split(" ");
        title = String.join(" ", words);

        Page<ProblemResponse> companyPage = problemRepository.findByTitleIgnoreCaseContainingAndIsPublicAndIsActive(title, pageable, true, true).map(problemMapper::toProblemResponse);

        return resultPaginationMapper.toResultPaginationResponse(companyPage);
    }

    public ResultPaginationResponse getCreated(Optional<Integer> page, Optional<Integer> pageSize) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<ProblemResponse> problemPage = problemRepository.findByCreatedByIdAndIsTheoryAndIsPublicAndIsActive(user.getId(),false, pageable, true, true).map(problemMapper::toProblemResponse);
        return resultPaginationMapper.toResultPaginationResponse(problemPage);
    }
    public void deleteProblem(Integer id){
        if (problemRepository.findById(id).isEmpty()){
            return;
        }
        Problem problem = problemRepository.getReferenceById(id);
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(problem.getCreatedBy() != user && !Objects.equals(user.getRole().getName(), "ADMIN")){
            throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
        }

        problem.setIsActive(false);
        problemRepository.save(problem);

    }

    public ProblemRunCodeResponse runTest(ProblemRunCodeRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId()).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND));

        List<ProblemTestcase> testcases = problem.getTestcases();
        List<RunCodeResponse> runCodeResponses = new ArrayList<>();
        String verdict = "Accepted";
        int passCount = 0;
        for (ProblemTestcase testcase : testcases){
            if(!testcase.getIsActive()){
                continue;
            }
            RunCodeResponse runCodeResponse = runCodeService.run(request.getLanguageId(),
                                                                request.getSourceCode(),
                                                                testcase.getInput(),
                                                                testcase.getExpectedOutput(),
                                                                (float) problem.getTimeLimitMs() /1000,
                                                                problem.getMemoryLimitKb()
                                                                );
            runCodeResponse.setTestCaseId(testcase.getId());
            if (!Objects.equals(runCodeResponse.getStatus(), "Accepted")){
                verdict = "Failed";
            }else {
                passCount++;
            }
            runCodeResponses.add(runCodeResponse);
        }
        return ProblemRunCodeResponse.builder().verdict(verdict)
                .problemId(problem.getId())
                .testCaseResult(runCodeResponses)
                .totalCount(runCodeResponses.size())
                .passedCount(passCount)
                .build();

    }
    public ProblemRunCodeResponse submitCode(ProblemRunCodeRequest request) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Problem problem = problemRepository.findById(request.getProblemId()).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND));
        if(problem.getIsTheory()){
            CourseProgress progress = courseProgressRepository.findByUserIdAndProblemId(user.getId(), problem.getId())
                    .orElseGet(()-> CourseProgress.builder()
                            .user(user)
                            .course(problem.getModule().getCourse())
                            .module(problem.getModule())
                            .problem(problem)
                            .status(ProblemCourseStatus.COMPLETED)
                            .completedAt(LocalDateTime.now())
                            .build());
            courseProgressRepository.save(progress);
            return ProblemRunCodeResponse.builder()
                    .verdict("Accepted")
                    .build();
        }

        ProblemRunCodeResponse runCodeResponse = runTest(request);
        Submission submission = Submission.builder()
                .languageId(runCodeResponse.getProblemId())
                .language(request.getLanguage())
                .sourceCode(request.getSourceCode())
                .createdAt(LocalDateTime.now())
                .verdict(runCodeResponse.getVerdict())
                .problem(problem)
                .user(user)
                .build();
        submissionRepository.save(submission);
        if(Objects.equals(runCodeResponse.getVerdict(), "Accepted") &&
                problem.getModule() == null &&
                problem.getContest() == null){
            if(problemDoneRepository.findByUserIdAndProblemId(user.getId(), problem.getId()).isPresent()){
                return runCodeResponse;
            }
            ProblemDone problemDone = problemDoneRepository.findByUserIdAndProblemId(user.getId(), problem.getId()).orElseGet(() -> ProblemDone.builder()
                    .user(user)
                    .problem(problem).build());
            problemDoneRepository.save(problemDone);
            return runCodeResponse;
        }
        if(problem.getModule() != null){
            CourseProgress progress = courseProgressRepository.findByUserIdAndProblemId(user.getId(), problem.getId())
                    .orElseGet(()-> CourseProgress.builder()
                            .user(user)
                            .course(problem.getModule().getCourse())
                            .module(problem.getModule())
                            .problem(problem)
                            .build());

            if (Objects.equals(submission.getVerdict(), "Accepted")){
                progress.setStatus(ProblemCourseStatus.COMPLETED);
                progress.setCompletedAt(LocalDateTime.now());
            }else {
                progress.setStatus(ProblemCourseStatus.IN_PROGRESS);
            }
            courseProgressRepository.save(progress);
        }
        if (problem.getContest() != null){

        }
        return runCodeResponse;
    }
    public ResultPaginationResponse getListDone(Optional<Integer> page, Optional<Integer> pageSize, String difficulty){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<ProblemDone> problemDones = problemDoneRepository.findByUserId(user.getId());
        List<ProblemResponse> problemResponses = new ArrayList<>();
        for (ProblemDone problemDone : problemDones){
            ProblemResponse problemResponse = problemMapper.toProblemResponse(problemDone.getProblem());
            problemResponses.add(problemResponse);
        }

        if(difficulty != null){
            problemResponses.removeIf(problemResponse -> !Objects.equals(problemResponse.getDifficulty(), difficulty));
        }

        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), problemResponses.size());
        final Page<ProblemResponse> responses = new PageImpl<>(problemResponses.subList(start, end), pageable, problemResponses.size());

        return resultPaginationMapper.toResultPaginationResponse(responses);
    }
    public Boolean isDone(Integer problemId){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        problemDoneRepository.findByUserIdAndProblemId(user.getId(), problemId)
                .orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_COMPLETED));
        return true;
    }
}

