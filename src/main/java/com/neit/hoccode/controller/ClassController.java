package com.neit.hoccode.controller;


import com.neit.hoccode.dto.ApiResponse;
import com.neit.hoccode.dto.request.ClassRequest;
import com.neit.hoccode.dto.response.ClassResponse;
import com.neit.hoccode.service.ClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/class")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Class API", description = "CRUD lớp học")
public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/add")
    public ApiResponse<ClassResponse> add(@RequestBody ClassRequest request){
        return ApiResponse.<ClassResponse>builder().data(classService.add(request)).build();
    }
    @PutMapping("/modify")
    public ApiResponse<ClassResponse> modify(@RequestBody ClassRequest request){
        return ApiResponse.<ClassResponse>builder().data(classService.modify(request)).build();
    }
    @GetMapping("/enroll")
    public ApiResponse<?> enroll(@RequestParam Integer classId){
        classService.enroll(classId);
        return ApiResponse.builder().build();
    }
    @GetMapping("/out-class")
    public ApiResponse<?> outClass(@RequestParam Integer classId){
        classService.outClass(classId);
        return ApiResponse.builder().build();
    }

}
