package com.kati.taskmanager.service;

import com.kati.taskmanager.entity.Project;
import com.kati.taskmanager.entity.Task;
import com.kati.taskmanager.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.kati.taskmanager.repository.ProjectRepository;
import com.kati.taskmanager.repository.TaskRepository;
import com.kati.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService{
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    // Constructor through which Spring automatically injects the required repositories
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public List<Task> getAllTasks(){
        return  taskRepository.findAll();
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public Task createTask(Task task){

        Long userId = task.getAssignedUser().getId();
        Long projectId = task.getProject().getId();

        User existingAssignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        task.setAssignedUser(existingAssignedUser);
        task.setProject(existingProject);

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask){

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        Long userId = updatedTask.getAssignedUser().getId();
        Long projectId = updatedTask.getProject().getId();

        User existingAssignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDeadline(updatedTask.getDeadline());
        existingTask.setAssignedUser(existingAssignedUser);
        existingTask.setProject(existingProject);

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        taskRepository.delete(existingTask);
    }
}
