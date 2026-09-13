package com.kati.taskmanager.service;

import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.task.TaskCreateRequest;
import com.kati.taskmanager.dto.task.TaskResponse;
import com.kati.taskmanager.dto.task.TaskUpdateRequest;
import com.kati.taskmanager.dto.user.UserResponse;
import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.entity.Task;
import com.kati.taskmanager.entity.User;
import com.kati.taskmanager.exception.ProjectNotFoundException;
import com.kati.taskmanager.exception.TaskNotFoundException;
import com.kati.taskmanager.exception.UserNotFoundException;
import com.kati.taskmanager.mapper.TaskMapper;
import com.kati.taskmanager.repository.ProjectRepository;
import com.kati.taskmanager.repository.TaskRepository;
import com.kati.taskmanager.repository.UserRepository;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasks_shouldReturnAllTasksSuccessfully() {

        User assignedUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        Project project =
                new Project(
                        "Add tests",
                        "In Progress",
                        LocalDate.of(2027, 1, 10)
                );

        Task task1 =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 20),
                        assignedUser,
                        project
                );

        Task task2 =
                new Task(
                        "Make test",
                        "Make one test",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        assignedUser,
                        project
                );

        UserResponse userResponse =
                new UserResponse(
                        1L,
                        "Anna",
                        "anna@example.com"
                );

        ProjectResponse projectResponse =
                new ProjectResponse(
                        1L,
                        "Add tests",
                        "In Progress",
                        LocalDate.of(2027, 1, 10)
                );

        TaskResponse taskResponse1 =
                new TaskResponse(
                        1L,
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 20),
                        userResponse,
                        projectResponse
                );
        TaskResponse taskResponse2 =
                new TaskResponse(
                        2L,
                        "Make test",
                        "Make one test",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        userResponse,
                        projectResponse
                );

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        when(taskMapper.toResponse(task1))
                .thenReturn(taskResponse1);

        when(taskMapper.toResponse(task2))
                .thenReturn(taskResponse2);

        List<TaskResponse> result =
                taskService.getAllTasks();

        assertEquals(
                List.of(taskResponse1, taskResponse2),
                result
        );

        verify(taskRepository).findAll();

        verify(taskMapper).toResponse(task1);

        verify(taskMapper).toResponse(task2);
    }

    @Test
    void getTaskById_shouldReturnTaskWhenTaskExists() {

        Long id = 1L;

        User assignedUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        Project project =
                new Project(
                        "Add Test",
                        "In Progress",
                        LocalDate.of(2027, 1, 20)
                );

        Task task =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        assignedUser,
                        project
                );

        UserResponse userResponse =
                new UserResponse(
                        1L,
                        "Anna",
                        "anna@example.com"
                );

        ProjectResponse projectResponse =
                new ProjectResponse(
                        1L,
                        "Add Test",
                        "In Progress",
                        LocalDate.of(2027, 1, 20)
                );

        TaskResponse taskResponse =
                new TaskResponse(
                        1L,
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        userResponse,
                        projectResponse
                );

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        when(taskMapper.toResponse(task))
                .thenReturn(taskResponse);

        TaskResponse result =
                taskService.getTaskById(id);

        assertEquals(taskResponse, result);

        verify(taskRepository).findById(id);

        verify(taskMapper).toResponse(task);
    }

    @Test
    void getTaskById_shouldThrowExceptionWhenTaskDoesNotExist() {

        Long id = 1L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(id)
        );

        verify(taskRepository).findById(id);

        verifyNoInteractions(taskMapper);
    }

    @Test
    void createTask_shouldCreateTaskSuccessfully() {

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
                        "Add Test",
                        "In Progress",
                        LocalDate.of(2027, 1, 20)
                );

        Task task =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        assignedUser,
                        project
                );

        Task savedTask =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        assignedUser,
                        project
                );

        UserResponse userResponse =
                new UserResponse(
                        1L,
                        "Anna",
                        "anna@example.com"
                );

        ProjectResponse projectResponse =
                new ProjectResponse(
                        2L,
                        "Add Test",
                        "In Progress",
                        LocalDate.of(2027, 1, 20)
                );

        TaskResponse taskResponse =
                new TaskResponse(
                        3L,
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        userResponse,
                        projectResponse
                );

        when(userRepository.findById(request.assignedUserId()))
                .thenReturn(Optional.of(assignedUser));

        when(projectRepository.findById(request.projectId()))
                .thenReturn(Optional.of(project));

        when(taskMapper.toEntity(request, assignedUser, project))
                .thenReturn(task);

        when(taskRepository.save(task))
                .thenReturn(savedTask);

        when(taskMapper.toResponse(savedTask))
                .thenReturn(taskResponse);

        TaskResponse result =
                taskService.createTask(request);

        assertEquals(taskResponse, result);

        verify(userRepository).findById(request.assignedUserId());

        verify(projectRepository).findById(request.projectId());

        verify(taskMapper).toEntity(request, assignedUser, project);

        verify(taskRepository).save(task);

        verify(taskMapper).toResponse(savedTask);
    }

    @Test
    void createTask_shouldThrowExceptionWhenUserDoesNotExist() {

        Long assignedUserId = 1L;
        Long projectId = 1L;

        TaskCreateRequest request =
                new TaskCreateRequest(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        assignedUserId,
                        projectId
                );

        when(userRepository.findById(request.assignedUserId()))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> taskService.createTask(request)
        );

        verify(userRepository).findById(request.assignedUserId());

        verifyNoInteractions(projectRepository);
        verifyNoInteractions(taskMapper);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void createTask_shouldThrowExceptionWhenProjectDoesNotExist() {

        Long assignedUserId = 1L;
        Long projectId = 1L;

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

        when(userRepository.findById(request.assignedUserId()))
                .thenReturn(Optional.of(assignedUser));

        when(projectRepository.findById(request.projectId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> taskService.createTask(request)
        );

        verify(userRepository).findById(request.assignedUserId());

        verify(projectRepository).findById(request.projectId());

        verifyNoInteractions(taskMapper);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void updateTask_shouldUpdateTaskSuccessfully() {

        Long id = 1L;
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

        User oldUser =
                new User(
                        "Old Assigned User",
                        "old.assigned.user@example.com"
                );

        Project oldProject =
                new Project(
                        "Old Project",
                        "Planned",
                        LocalDate.of(2026, 12, 31)
                );

        Task existingTask =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        oldUser,
                        oldProject
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

        Task savedTask =
                new Task(
                        "Write code today",
                        "Write new code today",
                        "In Progress",
                        LocalDate.of(2027, 3, 30),
                        assignedUser,
                        project
                );

        UserResponse assignedUserResponse =
                new UserResponse(
                        2L,
                        "Tomek",
                        "tomek@example.com"
                );

        ProjectResponse projectResponse =
                new ProjectResponse(
                        3L,
                        "Updated Test",
                        "In Progress",
                        LocalDate.of(2027, 2, 20)
                );

        TaskResponse taskResponse =
                new TaskResponse(
                        1L,
                        "Write code today",
                        "Write new code today",
                        "In Progress",
                        LocalDate.of(2027, 3, 30),
                        assignedUserResponse,
                        projectResponse
                );

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(existingTask));

        when(userRepository.findById(request.assignedUserId()))
                .thenReturn(Optional.of(assignedUser));

        when(projectRepository.findById(request.projectId()))
                .thenReturn(Optional.of(project));

        when(taskRepository.save(existingTask))
                .thenReturn(savedTask);

        when(taskMapper.toResponse(savedTask))
                .thenReturn(taskResponse);

        TaskResponse result =
                taskService.updateTask(id, request);

        assertEquals(taskResponse, result);

        verify(taskRepository).findById(id);
        verify(userRepository).findById(request.assignedUserId());
        verify(projectRepository).findById(request.projectId());
        verify(taskMapper).updateEntity(request, existingTask, assignedUser, project);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toResponse(savedTask);
    }

    @Test
    void updateTask_shouldThrowExceptionWhenTaskDoesNotExist() {

        Long id = 1L;
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

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateTask(id, request)
        );

        verify(taskRepository).findById(id);

        verifyNoInteractions(userRepository);
        verifyNoInteractions(projectRepository);
        verifyNoInteractions(taskMapper);

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowExceptionWhenUserNotExist() {

        Long id = 1L;
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

        User oldUser =
                new User(
                        "Old Assigned User",
                        "old.assigned.user@example.com"
                );

        Project oldProject =
                new Project(
                        "Old Project",
                        "Planned",
                        LocalDate.of(2026, 12, 31)
                );

        Task existingTask =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        oldUser,
                        oldProject
                );

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(existingTask));

        when(userRepository.findById(request.assignedUserId()))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> taskService.updateTask(id, request)
        );

        verify(taskRepository).findById(id);

        verify(userRepository).findById(request.assignedUserId());

        verifyNoInteractions(projectRepository);
        verifyNoInteractions(taskMapper);

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_shouldThrowExceptionWhenProjectNotExist() {

        Long id = 1L;
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

        User oldUser =
                new User(
                        "Old Assigned User",
                        "old.assigned.user@example.com"
                );

        Project oldProject =
                new Project(
                        "Old Project",
                        "Planned",
                        LocalDate.of(2026, 12, 31)
                );

        Task existingTask =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        oldUser,
                        oldProject
                );

        User assignedUser =
                new User(
                        "Tomek",
                        "tomek@example.com"
                );

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(existingTask));

        when(userRepository.findById(request.assignedUserId()))
                .thenReturn(Optional.of(assignedUser));

        when(projectRepository.findById(request.projectId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> taskService.updateTask(id, request)
        );

        verify(taskRepository).findById(id);

        verify(userRepository).findById(request.assignedUserId());

        verify(projectRepository).findById(request.projectId());

        verifyNoInteractions(taskMapper);

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldDeleteTaskSuccessfully() {

        Long id = 1L;

        User existingUser =
                new User(
                        "Tomek",
                        "tomek@example.com"
                );

        Project existingProject =
                new Project(
                        "Updated Test",
                        "In Progress",
                        LocalDate.of(2027, 2, 20)
                );

        Task existingTask =
                new Task(
                        "Write code",
                        "Write new code",
                        "Planned",
                        LocalDate.of(2027, 1, 30),
                        existingUser,
                        existingProject
                );

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(existingTask));

        taskService.deleteTask(id);

        verify(taskRepository).findById(id);

        verify(taskRepository).delete(existingTask);
    }

    @Test
    void deleteTask_shouldThrowExceptionWhenTaskDoesNotExist() {

        Long id = 1L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(id)
        );

        verify(taskRepository).findById(id);

        verify(taskRepository, never()).delete(any(Task.class));
    }
}
