package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.SubmissionRequest;
import com.neit.hoccode.entity.*;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final JudgeResultRepository judgeResultRepository;
    private final ProblemTestcaseRepository problemTestcaseRepository;
    private final UserRepository userRepository;

    public SubmissionService(SubmissionRepository submissionRepository, ProblemRepository problemRepository, JudgeResultRepository judgeResultRepository, ProblemTestcaseRepository problemTestcaseRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.judgeResultRepository = judgeResultRepository;
        this.problemTestcaseRepository = problemTestcaseRepository;
        this.userRepository = userRepository;
    }

    public JudgeResult submission(SubmissionRequest request){
        Submission submission = Submission.builder().createdAt(LocalDateTime.now()).language(request.getLanguage()).sourceCode(request.getSourceCode()).build();
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Problem problem = problemRepository.findById(request.getProblemId()).orElseThrow(()-> new AppException(ErrorCode.PROBLEM_NOT_FOUND));
        ProblemTestcase testcase = problemTestcaseRepository.findById(request.getTestcaseId()).orElseThrow(()->new AppException(ErrorCode.TESTCASE_NOT_FOUND));

        submission.setUser(user);
        submission.setProblem(problem);
        submissionRepository.save(submission);

//       ============ RUN CODE ==============

//       ====================================

        return judgeResultRepository.save(JudgeResult.builder().submission(submission).testcase(testcase).build());
    }
}
