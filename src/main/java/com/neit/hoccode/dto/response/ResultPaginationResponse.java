package com.neit.hoccode.dto.response;

import lombok.Data;

@Data
public class ResultPaginationResponse {
    private long total_records;
    private int total_records_page;
    private int current_page;
    private int total_pages;
    private int prev_pages;
    private int next_pages;
    private Object data;
}
