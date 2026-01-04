package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.response.ResultPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResultPaginationMapper {
    public ResultPaginationResponse toResultPaginationResponse(Page<?> resultPage) {
        ResultPaginationResponse response = new ResultPaginationResponse();
        response.setTotal_records(resultPage.getTotalElements());
        response.setTotal_records_page(resultPage.getNumberOfElements());
        response.setCurrent_page(resultPage.getNumber());
        response.setTotal_pages(resultPage.getTotalPages());
        response.setData(resultPage.getContent());
        if(resultPage.hasNext()){
            response.setNext_pages(response.getCurrent_page() + 1);
        }
        if(resultPage.hasPrevious()){
            response.setPrev_pages(response.getCurrent_page() - 1);
        }
        return response;
    }
    public Pageable toPageAble(Optional<Integer> page, Optional<Integer> pageSize){
        int sPage = page.orElse(1);
        int sPageSize = pageSize.orElse(10);
        
        if (sPage < 1){
            sPage = 1;
        }
        return PageRequest.of(sPage - 1, sPageSize);
    }
}
