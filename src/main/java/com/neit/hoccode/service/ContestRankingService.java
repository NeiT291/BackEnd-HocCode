package com.neit.hoccode.service;

import com.neit.hoccode.dto.response.ContestRankingResponse;
import com.neit.hoccode.dto.response.ProblemResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.entity.*;
import com.neit.hoccode.mapper.ResultPaginationMapper;
import com.neit.hoccode.mapper.UserMapper;
import com.neit.hoccode.repository.ContestRegistrationRepository;
import com.neit.hoccode.repository.ContestRepository;
import com.neit.hoccode.repository.ProblemRepository;
import com.neit.hoccode.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContestRankingService {

    private final ContestRepository contestRepository;
    private final ContestRegistrationRepository contestRegistrationRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final UserMapper userMapper;

    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 30;
    private static final int PENALTY_PER_WRONG = 5;

    public ResultPaginationResponse getContestRanking(Optional<Integer> page, Optional<Integer> pageSize, Integer contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        LocalDateTime startTime = contest.getStartTime();
        LocalDateTime endTime = contest.getEndTime();

        long contestDurationMinutes =
                Duration.between(startTime, endTime).toMinutes();

        List<ContestRegistration> registrations =
                contestRegistrationRepository.findByContestId(contestId);

        List<Problem> problems =
                problemRepository.findByContestIdAndIsActiveTrue(contestId);

        List<ContestRankingResponse> ranking = new ArrayList<>();

        for (ContestRegistration reg : registrations) {

            User user = reg.getUser();

            int solvedCount = 0;
            int totalScore = 0;
            int totalPenalty = 0;
            LocalDateTime lastAcceptedTime = null;

            for (Problem problem : problems) {

                List<Submission> acceptedSubs =
                        submissionRepository.findAcceptedSubmissions(
                                user.getId(),
                                problem.getId(),
                                startTime,
                                endTime
                        );

                if (acceptedSubs.isEmpty()) continue;

                Submission accepted = acceptedSubs.get(0);
                solvedCount++;

                // 1️⃣ Tính thời gian AC
                long minutesFromStart =
                        Duration.between(startTime, accepted.getCreatedAt())
                                .toMinutes();

                // 2️⃣ Tính điểm theo thời gian
                double ratio =
                        (double) minutesFromStart / contestDurationMinutes;

                int score =
                        (int) Math.round(
                                MAX_SCORE - ratio * (MAX_SCORE - MIN_SCORE)
                        );

                score = Math.max(score, MIN_SCORE);

                // 3️⃣ Penalty
                int wrongCount =
                        submissionRepository.countWrongBeforeAccepted(
                                user.getId(),
                                problem.getId(),
                                accepted.getCreatedAt()
                        );

                int penalty = wrongCount * PENALTY_PER_WRONG;

                score = Math.max(score - penalty, 0);

                totalPenalty += penalty;
                totalScore += score;

                if (lastAcceptedTime == null ||
                        accepted.getCreatedAt().isAfter(lastAcceptedTime)) {
                    lastAcceptedTime = accepted.getCreatedAt();
                }
            }

            ranking.add(
                    ContestRankingResponse.builder()
                            .user(userMapper.toUserResponse(user))
                            .totalScore(totalScore)
                            .solvedCount(solvedCount)
                            .penalty(totalPenalty)
                            .lastAcceptedTime(lastAcceptedTime)
                            .build()
            );
        }

        // 4️⃣ Sort ranking
        ranking.sort(
                Comparator
                        .comparing(ContestRankingResponse::getTotalScore, Comparator.reverseOrder())
                        .thenComparing(ContestRankingResponse::getSolvedCount, Comparator.reverseOrder())
                        .thenComparing(ContestRankingResponse::getPenalty)
                        .thenComparing(
                                ContestRankingResponse::getLastAcceptedTime,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
        );

        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), ranking.size());
        final Page<ContestRankingResponse> responses = new PageImpl<>(ranking.subList(start, end), pageable, ranking.size());

        return resultPaginationMapper.toResultPaginationResponse(responses);
    }
}
