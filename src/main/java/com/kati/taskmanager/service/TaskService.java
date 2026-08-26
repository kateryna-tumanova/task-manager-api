package com.kati.taskmanager.service;

import com.kati.taskmanager.dto.task.TaskCreateRequest;
import com.kati.taskmanager.dto.task.TaskResponse;
import com.kati.taskmanager.dto.task.TaskUpdateRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    // Constructor injection
    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TaskMapper taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        User assignedUser = userRepository.findById(request.assignedUserId())
                .orElseThrow(() -> new UserNotFoundException(request.assignedUserId()));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(request.projectId()));

        Task task = taskMapper.toEntity(request, assignedUser, project);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        User assignedUser = userRepository.findById(request.assignedUserId())
                .orElseThrow(() -> new UserNotFoundException(request.assignedUserId()));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(request.projectId()));

        taskMapper.updateEntity(request, existingTask, assignedUser, project);
        Task savedTask = taskRepository.save(existingTask);

        return taskMapper.toResponse(savedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(existingTask);
    }
}
