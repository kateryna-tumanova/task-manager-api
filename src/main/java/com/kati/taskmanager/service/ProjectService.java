package com.kati.taskmanager.service;

import com.kati.taskmanager.dto.project.ProjectCreateRequest;
import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.project.ProjectUpdateRequest;
import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.mapper.ProjectMapper;
import com.kati.taskmanager.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    // Constructor injection
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found"
                ));

        return projectMapper.toResponse(project);
    }

    public ProjectResponse createProject(ProjectCreateRequest request) {
        Project project = projectMapper.toEntity(request);
        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponse(savedProject);
    }

    public ProjectResponse updateProject(
            Long id,
            ProjectUpdateRequest request
    ) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found"
                ));

        projectMapper.updateEntity(request, existingProject);
        Project savedProject = projectRepository.save(existingProject);

        return projectMapper.toResponse(savedProject);
    }

    public void deleteProject(Long id) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found"
                ));

        projectRepository.delete(existingProject);
    }
}
