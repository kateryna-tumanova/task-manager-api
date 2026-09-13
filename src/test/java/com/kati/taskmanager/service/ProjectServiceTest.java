package com.kati.taskmanager.service;

import com.kati.taskmanager.dto.project.ProjectCreateRequest;
import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.project.ProjectUpdateRequest;
import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.exception.ProjectNotFoundException;
import com.kati.taskmanager.mapper.ProjectMapper;
import com.kati.taskmanager.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjects_shouldReturnAllProjectsSuccessfully() {

        Project project1 =
                new Project(
                        "Test Project",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        Project project2 =
                new Project(
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2028, 3, 14)
                );

        ProjectResponse response1 =
                new ProjectResponse(
                        1L,
                        "Test Project",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        ProjectResponse response2 =
                new ProjectResponse(
                        2L,
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2028, 3, 14)
                );

        when(projectRepository.findAll())
                .thenReturn(List.of(project1, project2));

        when(projectMapper.toResponse(project1))
                .thenReturn(response1);

        when(projectMapper.toResponse(project2))
                .thenReturn(response2);

        List<ProjectResponse> result =
                projectService.getAllProjects();

        assertEquals(
                List.of(response1, response2),
                result
        );

        verify(projectRepository).findAll();
        verify(projectMapper).toResponse(project1);
        verify(projectMapper).toResponse(project2);
    }

    @Test
    void getProjectById_shouldReturnProjectWhenProjectExists() {

        Long id = 3L;
        Project project =
                new Project(
                        "Test Project 2",
                        "Planned",
                        LocalDate.of(2028, 1, 14)
                );

        ProjectResponse response =
                new ProjectResponse(
                        3L,
                        "Test Project 2",
                        "Planned",
                        LocalDate.of(2028, 1, 14)
                );

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        ProjectResponse result = projectService.getProjectById(id);

        assertEquals(
                response,
                result
        );

        verify(projectRepository).findById(id);

        verify(projectMapper).toResponse(project);
    }

    @Test
    void getProjectById_shouldThrowExceptionWhenProjectDoesNotExist() {

        Long id = 5L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.getProjectById(id)
        );

        verify(projectRepository).findById(id);

        verify(projectMapper, never()).toResponse(any(Project.class));
    }

    @Test
    void createProject_shouldCreateProjectSuccessfully() {

        ProjectCreateRequest request =
                new ProjectCreateRequest(
                        "Task Manager API 2",
                        "Planned",
                        LocalDate.of(2027, 3, 14)
                );

        Project project =
                new Project(
                        "Task Manager API 2",
                        "Planned",
                        LocalDate.of(2027, 3, 14)
                );

        Project savedProject =
                new Project(
                        "Task Manager API 2",
                        "Planned",
                        LocalDate.of(2027, 3, 14)
                );

        ProjectResponse response =
                new ProjectResponse(
                        1L,
                        "Task Manager API 2",
                        "Planned",
                        LocalDate.of(2027, 3, 14)
                );

        when(projectMapper.toEntity(request))
                .thenReturn(project);

        when(projectRepository.save(project))
                .thenReturn(savedProject);

        when(projectMapper.toResponse(savedProject))
                .thenReturn(response);

        ProjectResponse result = projectService.createProject(request);

        assertEquals(
                response,
                result
        );

        verify(projectMapper).toEntity(request);

        verify(projectRepository).save(project);

        verify(projectMapper).toResponse(savedProject);
    }

    @Test
    void updateProject_shouldUpdateProjectSuccessfully() {

        Long id = 5L;

        ProjectUpdateRequest request =
                new ProjectUpdateRequest(
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2027, 1, 14)
                );

        Project existingProject =
                new Project(
                        "Task Manager API",
                        "Planned",
                        LocalDate.of(2027, 3, 14)
                );

        Project savedProject =
                new Project(
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2027, 1, 14)
                );

        ProjectResponse response =
                new ProjectResponse(
                        5L,
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2027, 1, 14)
                );

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(existingProject));

        when(projectRepository.save(existingProject))
                .thenReturn(savedProject);

        when(projectMapper.toResponse(savedProject))
                .thenReturn(response);

        ProjectResponse result = projectService.updateProject(id, request);

        assertEquals(
                response,
                result
        );

        verify(projectRepository).findById(id);

        verify(projectMapper).updateEntity(request, existingProject);

        verify(projectRepository).save(existingProject);

        verify(projectMapper).toResponse(savedProject);
    }

    @Test
    void updateProject_shouldThrowExceptionWhenProjectDoesNotExist() {

        Long id = 5L;

        ProjectUpdateRequest request =
                new ProjectUpdateRequest(
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2027, 1, 14)
                );

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.updateProject(id, request)
        );

        verify(projectRepository).findById(id);

        verify(projectRepository, never()).save(any(Project.class));

        verifyNoInteractions(projectMapper);
    }

    @Test
    void deleteProject_shouldDeleteProjectSuccessfully() {

        Long id = 1L;

        Project existingProject =
                new Project(
                        "Task Manager API",
                        "In Progress",
                        LocalDate.of(2027, 1, 14)
                );

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(existingProject));

        projectService.deleteProject(id);

        verify(projectRepository).findById(id);

        verify(projectRepository).delete(existingProject);
    }

    @Test
    void deleteProject_shouldThrowExceptionWhenProjectDoesNotExist() {

        Long id = 1L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.deleteProject(id)
        );

        verify(projectRepository).findById(id);

        verify(projectRepository, never())
                .delete(any(Project.class));

        verifyNoInteractions(projectMapper);
    }
}
