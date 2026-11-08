
package com.freelancehub.service;

import com.freelancehub.model.Client;
import com.freelancehub.model.Project;
import com.freelancehub.repository.ProjectRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(
            String title,
            String description,
            double budget,
            LocalDate deadline,
            Client client) {

        if (budget <= 0) {
            throw new IllegalArgumentException(
                    "Budget must be greater than zero"
            );
        }

        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Deadline cannot be in the past"
            );
        }

        Project project = new Project(
                title, description, budget, deadline, client
        );

        return projectRepository.save(project);
    }

public java.util.List<Project> getOpenProjects() {
    return projectRepository.findByStatus("OPEN");
}

public java.util.List<Project> getProjectsByClient(Long clientId) {
    return projectRepository.findAll().stream()
            .filter(project -> project.getClient().getId().equals(clientId))
            .toList();
}

}
