package com.kati.taskmanager.mapper;

import com.kati.taskmanager.dto.task.TaskCreateRequest;
import com.kati.taskmanager.dto.task.TaskResponse;
import com.kati.taskmanager.dto.task.TaskUpdateRequest;
import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.entity.Task;
import com.kati.taskmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    public TaskMapper(UserMapper userMapper, ProjectMapper projectMapper) {
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
    }

    public Task toEntity(
            TaskCreateRequest request,
            User assignedUser,
            Project project
    ) {
        return new Task(
                request.title(),
                request.description(),
                request.status(),
                request.deadline(),
                assignedUser,
                project
        );
    }

    public void updateEntity(
            TaskUpdateRequest request,
            Task task,
            User assignedUser,
            Project project
    ) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDeadline(request.deadline());
        task.setAssignedUser(assignedUser);
        task.setProject(project);
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                userMapper.toResponse(task.getAssignedUser()),
                projectMapper.toResponse(task.getProject())
        );
    }
}
