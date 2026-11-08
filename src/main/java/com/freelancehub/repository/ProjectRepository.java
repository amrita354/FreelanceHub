
package com.freelancehub.repository;

import com.freelancehub.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    // Get all projects with a particular status
    List<Project> findByStatus(String status);

    // Get all projects posted by a particular client
    List<Project> findByClient_Id(Long clientId);
}
