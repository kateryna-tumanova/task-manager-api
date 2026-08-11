package com.kati.taskmanager.controller;

import com.kati.taskmanager.dto.project.ProjectCreateRequest;
import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.project.ProjectUpdateRequest;
import com.kati.taskmanager.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    public ProjectResponse createProject(
            @RequestBody ProjectCreateRequest request
    ) {
        return projectService.createProject(request);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(
            @PathVariable Long id,
            @RequestBody ProjectUpdateRequest request
    ) {
        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
