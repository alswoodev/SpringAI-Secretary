package com.spring.ai.basic.repository;

import java.time.LocalDate;
import java.util.List;

import com.spring.ai.basic.entity.Task;
import com.spring.ai.basic.entity.enums.task.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TaskRepository extends JpaRepository<Task, Long> {
    //쿼리 내부에서 경로기반 ENUM 로딩 불가. 파라미터로 받기
    @Query("""
        SELECT t FROM Task t
        WHERE t.user.userId = :userId
        AND t.startDate BETWEEN :from AND :to
        AND t.status = :status
        ORDER BY t.startDate ASC
    """)
    List<Task> findTask(
        @Param("userId") String userId,
        @Param("from") LocalDate startDate,
        @Param("to") LocalDate endDate,
        @Param("status") TaskStatus status
    );

    @Query("SELECT t FROM Task t WHERE t.user.userId = :userId AND t.priority = :priority AND t.status = :status ORDER BY t.startDate ASC")
    List<Task> findTaskWithPriority(@Param("userId") String userId, @Param("priority") String priority, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.user.userId = :userId AND t.startDate >= CURRENT_DATE ORDER BY t.startDate ASC")
    List<Task> findUpcomingTasks(@Param("userId") String userId);

    List<Task> findByUserUserId(String userId);

}
