package com.neit.hoccode.dto.response.admin;

import lombok.Data;

@Data
public class DashboardResponse {
    private Long totalContest;
    private Long totalCourse;
    private Long totalClass;
    private Long totalUser;
    private Long totalProblem;
}
