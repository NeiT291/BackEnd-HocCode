package com.neit.hoccode.dto.request;
import com.neit.hoccode.entity.CourseModule;
import lombok.Data;


@Data
public class LessonRequest {
    private Integer id;
    private String title;
    private String content;
    private Integer position = 0;
    private Integer moduleId;
}
