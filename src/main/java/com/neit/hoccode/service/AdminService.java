package com.neit.hoccode.service;

import com.neit.hoccode.dto.response.ContestResponse;
import com.neit.hoccode.dto.response.CourseResponse;
import com.neit.hoccode.dto.response.ResultPaginationResponse;
import com.neit.hoccode.dto.response.UserResponse;
import com.neit.hoccode.dto.response.admin.DashboardResponse;
import com.neit.hoccode.entity.*;
import com.neit.hoccode.mapper.*;
import com.neit.hoccode.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ProblemRepository problemRepository;
    private final ContestRegistrationRepository contestRegistrationRepository;
    private final ResultPaginationMapper resultPaginationMapper;
    private final CourseMapper courseMapper;
    private final ProblemMapper problemMapper;
    private final ContestMapper contestMapper;
    private final UserMapper userMapper;


    public AdminService(ContestRepository contestRepository, UserRepository userRepository, CourseRepository courseRepository, ProblemRepository problemRepository, ContestRegistrationRepository contestRegistrationRepository, ResultPaginationMapper resultPaginationMapper, CourseMapper courseMapper, ProblemMapper problemMapper, ContestMapper contestMapper, UserMapper userMapper) {
        this.contestRepository = contestRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.problemRepository = problemRepository;
        this.contestRegistrationRepository = contestRegistrationRepository;
        this.resultPaginationMapper = resultPaginationMapper;
        this.courseMapper = courseMapper;
        this.problemMapper = problemMapper;
        this.contestMapper = contestMapper;
        this.userMapper = userMapper;
    }

    public DashboardResponse getDashboard(){
        DashboardResponse response = new DashboardResponse();
        response.setTotalCourse(courseRepository.count());
        response.setTotalContest(contestRepository.count());
        response.setTotalProblem(problemRepository.count());
        response.setTotalUser(userRepository.count());

        return response;
    }
//    ================== COURSE =====================
    public ResultPaginationResponse getAllCourse(Optional<Integer> page, Optional<Integer> pageSize, String title, Boolean isActive){
        List<Course> courses = courseRepository.findAll();
        if( title!= null){
            String finalTitle = String.join(" ", title.split(" "));
            courses.removeIf( course ->
                    !course.getTitle().toLowerCase().contains(finalTitle.toLowerCase())
            );
        }
        if( isActive!= null){
            if(isActive){
                courses.removeIf( course -> !course.getIsActive());
            } else {
                courses.removeIf( course -> course.getIsActive());
            };
        }

        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), courses.size());
        final Page<Course> coursePage = new PageImpl<>(courses.subList(start, end), pageable, courses.size());

        return resultPaginationMapper.toResultPaginationResponse(coursePage.map(courseMapper::toCourseResponse));
    }
    public void deleteCourses(List<Integer> listCourseId){
        for (Integer id : listCourseId){
            if (courseRepository.findById(id).isEmpty()){
                continue;
            }
            Course course = courseRepository.getReferenceById(id);
            course.setIsActive(false);
            courseRepository.save(course);
        }
    }
    public void restoreCourses(List<Integer> listCourseId){
        for (Integer id : listCourseId){
            if (courseRepository.findById(id).isEmpty()){
                continue;
            }
            Course course = courseRepository.getReferenceById(id);
            course.setIsActive(true);
            courseRepository.save(course);
        }
    }
    //    ================== Problem =====================

    public ResultPaginationResponse getAllProblem(Optional<Integer> page, Optional<Integer> pageSize, String title, Boolean isActive){
        List<Problem> problems = problemRepository.findAll();
        if( title!= null){
            String finalTitle = String.join(" ", title.split(" "));
            problems.removeIf( problem ->
                    !problem.getTitle().toLowerCase().contains(finalTitle.toLowerCase())
            );
        }
        if( isActive!= null){
            if(isActive){
                problems.removeIf( problem -> !problem.getIsActive());
            } else {
                problems.removeIf( problem -> problem.getIsActive());
            };
        }

        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), problems.size());
        final Page<Problem> problemPage = new PageImpl<>(problems.subList(start, end), pageable, problems.size());

        return resultPaginationMapper.toResultPaginationResponse(problemPage.map(problemMapper::toProblemResponse));
    }
    public void deleteProblems(List<Integer> listProblemId){
        for (Integer id : listProblemId){
            if (problemRepository.findById(id).isEmpty()){
                continue;
            }
            Problem problem = problemRepository.getReferenceById(id);
            problem.setIsActive(false);
            problemRepository.save(problem);
        }
    }
    public void restoreProblems(List<Integer> listProblemId){
        for (Integer id : listProblemId){
            if (problemRepository.findById(id).isEmpty()){
                continue;
            }
            Problem problem = problemRepository.getReferenceById(id);
            problem.setIsActive(true);
            problemRepository.save(problem);
        }
    }
    //    ================== Contest =====================

    public ResultPaginationResponse getAllContest(Optional<Integer> page, Optional<Integer> pageSize, String title, Boolean isActive){
        List<Contest> contests = contestRepository.findAll();
        if( title!= null){
            String finalTitle = String.join(" ", title.split(" "));
            contests.removeIf( contest ->
                    !contest.getTitle().toLowerCase().contains(finalTitle.toLowerCase())
            );
        }
        if( isActive!= null){
            if(isActive){
                contests.removeIf( problem -> !problem.getIsActive());
            } else {
                contests.removeIf( problem -> problem.getIsActive());
            };
        }

        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), contests.size());
        final Page<Contest> contestPage = new PageImpl<>(contests.subList(start, end), pageable, contests.size());
        Page<ContestResponse> contestResponsePage = contestPage.map(contest -> {
            ContestResponse res = contestMapper.toContestResponse(contest);
            List<ContestRegistration> registrations = contestRegistrationRepository.findByContest(contest);

            List<UserResponse> userResponses = new ArrayList<>();
            for (ContestRegistration registration : registrations){
                userResponses.add(userMapper.toUserResponse(registration.getUser()));
            }
            res.setTotalUserEnroll(userResponses.size());
            res.setUserEnroll(userResponses);
            return res;
        });

        return resultPaginationMapper.toResultPaginationResponse(contestResponsePage);
    }
    public void deleteContests(List<Integer> listContestId){
        for (Integer id : listContestId){
            if (contestRepository.findById(id).isEmpty()){
                continue;
            }
            Contest contest = contestRepository.getReferenceById(id);
            contest.setIsActive(false);
            contestRepository.save(contest);
        }
    }
    public void restoreContests(List<Integer> listContestId){
        for (Integer id : listContestId){
            if (contestRepository.findById(id).isEmpty()){
                continue;
            }
            Contest contest = contestRepository.getReferenceById(id);
            contest.setIsActive(true);
            contestRepository.save(contest);
        }
    }
    //    ================== User =====================

    public ResultPaginationResponse getAllUser(Optional<Integer> page, Optional<Integer> pageSize, String username, Boolean isActive){
        List<User> users = userRepository.findAll();
        users.removeIf( user -> Objects.equals(user.getRole().getName(), "ADMIN"));
        if( username!= null){
            String finalUsername = String.join(" ", username.split(" "));
            users.removeIf( user ->
                    !user.getUsername().toLowerCase().contains(finalUsername.toLowerCase())
            );
        }
        if( isActive!= null){
            if(isActive){
                users.removeIf( user -> !user.getIsActive());
            } else {
                users.removeIf( user -> user.getIsActive());
            };
        }

        Pageable pageable = resultPaginationMapper.toPageAble(page, pageSize);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), users.size());
        final Page<User> userPage = new PageImpl<>(users.subList(start, end), pageable, users.size());

        return resultPaginationMapper.toResultPaginationResponse(userPage.map(userMapper::toUserResponse));
    }
    public void deleteUsers(List<String> listUsername){
        for (String username : listUsername){
            if (userRepository.findByUsername(username).isEmpty()){
                continue;
            }
            User user = userRepository.getByUsername(username);
            user.setIsActive(false);
            userRepository.save(user);
        }
    }
    public void restoreUsers(List<String> listUsername){
        for (String username : listUsername){
            if (userRepository.findByUsername(username).isEmpty()){
                continue;
            }
            User user = userRepository.getByUsername(username);
            user.setIsActive(true);
            userRepository.save(user);
        }
    }
}
