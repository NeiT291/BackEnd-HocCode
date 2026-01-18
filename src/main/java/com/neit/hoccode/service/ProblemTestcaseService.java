package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.ProblemTestcaseRequest;
import com.neit.hoccode.entity.Problem;
import com.neit.hoccode.entity.ProblemTestcase;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.ProblemTestcaseMapper;
import com.neit.hoccode.repository.ProblemRepository;
import com.neit.hoccode.repository.ProblemTestcaseRepository;
import com.neit.hoccode.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProblemTestcaseService {
    private final ProblemRepository problemRepository;
    private final ProblemTestcaseMapper problemTestcaseMapper;
    private final ProblemTestcaseRepository problemTestcaseRepository;
    private final UserRepository userRepository;
    public ProblemTestcaseService(ProblemRepository problemRepository, ProblemTestcaseMapper problemTestcaseMapper, ProblemTestcaseRepository problemTestcaseRepository, UserRepository userRepository) {
        this.problemRepository = problemRepository;
        this.problemTestcaseMapper = problemTestcaseMapper;
        this.problemTestcaseRepository = problemTestcaseRepository;
        this.userRepository = userRepository;
    }

    public ProblemTestcase add(ProblemTestcaseRequest request){
        Problem problem = problemRepository.findById(request.getProblemId()).orElseThrow(()->new AppException(ErrorCode.PROBLEM_NOT_FOUND));
        ProblemTestcase testcase = problemTestcaseMapper.toProblemTestcase(request);
        testcase.setProblem(problem);
        return problemTestcaseRepository.save(testcase);
    }
    public ProblemTestcase modify(ProblemTestcaseRequest request){
        ProblemTestcase testcase = problemTestcaseRepository.findById(request.getId()).orElseThrow(()-> new AppException(ErrorCode.TESTCASE_NOT_FOUND));
        testcase.setInput(request.getInput());
        testcase.setExpectedOutput(request.getExpectedOutput());
        testcase.setPosition(request.getPosition());
        return problemTestcaseRepository.save(testcase);
    }
    public void deleteTestcase(Integer id){
        if (problemTestcaseRepository.findById(id).isEmpty()){
            return;
        }
        ProblemTestcase testcase = problemTestcaseRepository.getReferenceById(id);

        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(testcase.getProblem().getCreatedBy() == user){
            testcase.setActive(false);
            problemTestcaseRepository.save(testcase);
        }
    }
}
