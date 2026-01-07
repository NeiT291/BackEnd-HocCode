package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.ContestRequest;
import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.dto.response.UserResponse;
import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.ContestRegistration;
import com.neit.hoccode.entity.Course;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.ContestMapper;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.mapper.UserMapper;
import com.neit.hoccode.repository.ContestRegistrationRepository;
import com.neit.hoccode.repository.ContestRepository;
import com.neit.hoccode.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContestService {
    private final ContestRepository contestRepository;
    private final ContestMapper contestMapper;
    private final UserRepository userRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final ContestRegistrationRepository contestRegistrationRepository;
    private final UserMapper userMapper;
    private final MinioService minioService;


    public ContestService(ContestRepository contestRepository, ContestMapper contestMapper, UserRepository userRepository, ResultPaginationMapper resultPaginationMapper, ContestRegistrationRepository contestRegistrationRepository, UserMapper userMapper, MinioService minioService) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
        this.userRepository = userRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.contestRegistrationRepository = contestRegistrationRepository;
        this.userMapper = userMapper;
        this.minioService = minioService;
    }

    public ContestResponse add(ContestRequest request) {
        Contest contest = contestMapper.toContest(request);
        if(contestRepository.getBySlug(request.getSlug()).isPresent()){
            throw new AppException(ErrorCode.CONTEST_SLUG_EXITED);
        }
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        contest.setCreatedAt(LocalDateTime.now());
        contest.setIsPublic(true);
        contest.setCreatedBy(user);

        return contestMapper.toContestResponse(contestRepository.save(contest));
    }
    public ContestResponse modify(ContestRequest request) {
        Contest contest = contestRepository.findById(request.getId()).orElseThrow(()->new AppException(ErrorCode.CONTEST_NOT_FOUND));
        contest.setTitle(request.getTitle());
        contest.setSlug(request.getSlug());
        contest.setDescription(request.getDescription());
        contest.setStartTime(request.getStartTime());
        contest.setEndTime(request.getEndTime());
        return contestMapper.toContestResponse(contestRepository.save(contest));
    }

    public ContestResponse getBySlug(String slug) {
        Contest contest = contestRepository.getBySlug(slug).orElseThrow(()-> new AppException(ErrorCode.CONTEST_NOT_FOUND));
        List<ContestRegistration> contestRegistrations = contestRegistrationRepository.findByContest(contest);
        List<UserResponse> userEnroll = new ArrayList<>();
        for(ContestRegistration contestRegistration : contestRegistrations){
            userEnroll.add(userMapper.toUserResponse(contestRegistration.getUser()));
        }
        ContestResponse contestResponse = contestMapper.toContestResponse(contest);
        contestResponse.setUserEnroll(userEnroll);
        return contestResponse;
    }
    public ContestRegistration enroll(int contestId){
        Contest contest = contestRepository.findById(contestId).orElseThrow(()-> new AppException(ErrorCode.CONTEST_NOT_FOUND));
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ContestRegistration contestRegistration = contestRegistrationRepository.findByUserAndContest(user, contest);
        if(contestRegistration != null){
            return contestRegistration ;
        }
        return contestRegistrationRepository.save(ContestRegistration.builder().contest(contest).user(user).registeredAt(LocalDateTime.now()).build());
    }
    public ResultPaginationResponse getAll(Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        Page<ContestResponse> contestPage = contestRepository.findAll(pageable).map(contestMapper::toContestResponse);
        return resultPaginationMapper.toResultPaginationResponse(contestPage);
    }
    public ResultPaginationResponse getByTitle(String title, Optional<Integer> page, Optional<Integer> pageSize){
        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        String[] words = title.split(" ");
        title = String.join(" ", words);

        Page<ContestResponse> companyPage = contestRepository.findByTitleIgnoreCaseContaining(title, pageable).map(contestMapper::toContestResponse);

        return resultPaginationMapper.toResultPaginationResponse(companyPage);
    }
    public Void setThumbnail(Integer contestId, MultipartFile thumbnail) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Contest contest = contestRepository.findById(contestId).orElseThrow(()-> new AppException(ErrorCode.CONTEST_NOT_FOUND));

        try{
            String objectName = minioService.uploadImage(thumbnail);
            String url = "http://localhost:9000/hoccode/" + objectName;
            contest.setThumbnailUrl(url);
            contestRepository.save(contest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
