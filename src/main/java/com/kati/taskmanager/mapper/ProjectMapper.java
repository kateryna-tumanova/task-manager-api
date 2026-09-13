package com.kati.taskmanager.mapper;

import com.kati.taskmanager.dto.project.ProjectCreateRequest;
import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.project.ProjectUpdateRequest;
import com.kati.taskmanager.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectCreateRequest request) {
        return new Project(
                request.name(),
                request.status(),
                request.deadline()
        );
    }

    public void updateEntity(ProjectUpdateRequest request, Project project) {
        project.setName(request.name());
        project.setStatus(request.status());
        project.setDeadline(request.deadline());
    }

    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getDeadline()
        );
    }
}
