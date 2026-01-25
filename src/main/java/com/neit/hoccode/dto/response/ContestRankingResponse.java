package com.neit.hoccode.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContestRankingResponse {
    private UserResponse user;

    private Integer totalScore;      // tổng điểm
    private Integer solvedCount;

    private Integer penalty;
    private LocalDateTime lastAcceptedTime; // tie-break
}
