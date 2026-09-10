package com.kati.taskmanager.mapper;

import com.kati.taskmanager.dto.task.TaskCreateRequest;
import com.kati.taskmanager.dto.task.TaskResponse;
import com.kati.taskmanager.dto.task.TaskUpdateRequest;
import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.entity.Task;
import com.kati.taskmanager.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final UserMapper userMapper = new UserMapper();
    private final ProjectMapper projectMapper = new ProjectMapper();

    private final TaskMapper taskMapper = new TaskMapper(userMapper, projectMapper);

    @Test
    void toEntity_shouldCreateTaskSuccessfully() {

        Long assignedUserId = 1L;
        Long projectId = 2L;

        TaskCreateRequest request =
                new TaskCreateRequest(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        assignedUserId,
                        projectId
                );

        User assignedUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        Project project =
                new Project(
                        "Test",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        Task result =
                taskMapper.toEntity(request, assignedUser, project);

        assertEquals(
                request.title(),
                result.getTitle()
        );

        assertEquals(
                request.description(),
                result.getDescription()
        );

        assertEquals(
                request.status(),
                result.getStatus()
        );

        assertEquals(
                request.deadline(),
                result.getDeadline()
        );

        assertSame(
                assignedUser,
                result.getAssignedUser()
        );

        assertSame(
                project,
                result.getProject()
        );
    }

    @Test
    void updateEntity_shouldUpdateTaskSuccessfully() {

        Long assignedUserId = 2L;
        Long projectId = 3L;

        TaskUpdateRequest request =
                new TaskUpdateRequest(
                        "Write code today",
                        "Write new code today",
                        "In Progress",
                        LocalDate.of(2027, 3, 30),
                        assignedUserId,
                        projectId
                );

        User existingUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        Project existingProject =
                new Project(
                        "Test",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        Task task =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        existingUser,
                        existingProject
                );

        User assignedUser =
                new User(
                        "Tomek",
                        "tomek@example.com"
                );

        Project project =
                new Project(
                        "Updated Test",
                        "In Progress",
                        LocalDate.of(2027, 2, 20)
                );

        taskMapper.updateEntity(request, task, assignedUser, project);

        assertEquals(
                request.title(),
                task.getTitle()
        );

        assertEquals(
                request.description(),
                task.getDescription()
        );

        assertEquals(
                request.status(),
                task.getStatus()
        );

        assertEquals(
                request.deadline(),
                task.getDeadline()
        );

        assertSame(
                assignedUser,
                task.getAssignedUser()
        );

        assertSame(
                project,
                task.getProject()
        );
    }

    @Test
    void toResponse_shouldReturnTaskResponseSuccessfully() {

        User assignedUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        Project project =
                new Project(
                        "Test",
                        "Planned",
                        LocalDate.of(2027, 1, 15)
                );

        Task task =
                new Task(
                        "Write code today",
                        "Write new code today",
                        "In Progress",
                        LocalDate.of(2027, 3, 30),
                        assignedUser,
                        project
                );

        TaskResponse result = taskMapper.toResponse(task);

        assertNull(result.id());

        assertEquals(
                task.getTitle(),
                result.title()
        );

        assertEquals(
                task.getDescription(),
                result.description()
        );

        assertEquals(
                task.getStatus(),
                result.status()
        );

        assertEquals(
                task.getDeadline(),
                result.deadline()
        );

        assertNull(result.assignedUser().id());

        assertEquals(
                assignedUser.getName(),
                result.assignedUser().name()
        );

        assertEquals(
                assignedUser.getEmail(),
                result.assignedUser().email()
        );

        assertNull(result.project().id());

        assertEquals(
                project.getName(),
                result.project().name()
        );

        assertEquals(
                project.getStatus(),
                result.project().status()
        );

        assertEquals(
                project.getDeadline(),
                result.project().deadline()
        );
    }
}
