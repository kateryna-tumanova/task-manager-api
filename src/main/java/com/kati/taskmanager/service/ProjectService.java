package com.kati.taskmanager.service;

import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProjectService{

    private final ProjectRepository projectRepository;

    // Constructor injection
    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }
    public Project getProjectById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }
    public Project createProject(Project project){
        return projectRepository.save(project);
    }
    public Project updateProject(Long id, Project updatedProject){
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        existingProject.setName(updatedProject.getName());
        existingProject.setStatus(updatedProject.getStatus());
        existingProject.setDeadline(updatedProject.getDeadline());

        return projectRepository.save(existingProject);
    }
    public void deleteProject(Long id){
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        projectRepository.delete(existingProject);
    }
}
