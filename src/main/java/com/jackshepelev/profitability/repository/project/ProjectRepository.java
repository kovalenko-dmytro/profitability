package com.jackshepelev.profitability.repository.project;

import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProjectRepository extends CommonRepository<Project> {
    @Modifying
    @Query(value = "INSERT INTO projects (user_id, title, created) VALUES (?1, ?2, ?3)", nativeQuery = true)
    int save(long userID, String title, LocalDateTime date);

    Project findByTitle(String title);
}
