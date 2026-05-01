package com.csbu.projects.Repositories;

import com.csbu.projects.Models.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Modifying
    @Transactional
    @Query("UPDATE Task a SET  a.status = true WHERE a.id = :id")
    void updateStatusById(@Param("id") String id);
    Page<Task> findByEmployeeId(String employeeId, Pageable pageable);


}
