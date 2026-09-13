package com.kati.taskmanager.mapper;

import com.kati.taskmanager.dto.project.ProjectCreateRequest;
import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.project.ProjectUpdateRequest;
import com.kati.taskmanager.entity.Project;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProjectMapperTest {

    private final ProjectMapper projectMapper = new ProjectMapper();

    @Test
    void toEntity_shouldCreateProjectSuccessfully() {

        ProjectCreateRequest request =
                new ProjectCreateRequest(
                        "Updated Test",
                        "In Progress",
                        LocalDate.of(2027, 2, 20)
                );

        Project result = projectMapper.toEntity(request);

        assertEquals(
                request.name(),
                result.getName()
        );

        assertEquals(
                request.status(),
                result.getStatus()
        );

        assertEquals(
                request.deadline(),
                result.getDeadline()
        );
    }

    @Test
    void updateEntity_shouldUpdateProjectSuccessfully() {

        ProjectUpdateRequest request =
                new ProjectUpdateRequest(
                        "Updated Test",
                        "In Progress",
                        LocalDate.of(2027, 1, 30)
                );

        Project project =
                new Project(
                        "Test",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        projectMapper.updateEntity(request, project);

        assertEquals(
                request.name(),
                project.getName()
        );

        assertEquals(
                request.status(),
                project.getStatus()
        );

        assertEquals(
                request.deadline(),
                project.getDeadline()
        );
    }

    @Test
    void toResponse_shouldReturnProjectResponseSuccessfully() {

        Project project =
                new Project(
                        "Test",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        ProjectResponse result =
                projectMapper.toResponse(project);

        assertNull(result.id());

        assertEquals(
                project.getName(),
                result.name()
        );

        assertEquals(
                project.getStatus(),
                result.status()
        );

        assertEquals(
                project.getDeadline(),
                result.deadline()
        );
    }
}
