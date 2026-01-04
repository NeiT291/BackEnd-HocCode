package com.neit.hoccode.repository;

import com.neit.hoccode.entity.Lesson;
import com.neit.hoccode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
