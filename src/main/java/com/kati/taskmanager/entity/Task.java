package com.kati.taskmanager.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 100)
    private String status;

    @Column(nullable = false)
    private LocalDate deadline;

    @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
    private User assignedUser;

    @ManyToOne
        @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Task(){
    }

    public Task(
        String title,
        String description,
        String status,
        LocalDate deadline,
        User assignedUser,
        Project project){

        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.assignedUser = assignedUser;
        this.project = project;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public LocalDate getDeadline(){
        return deadline;
    }
    public void setDeadline(LocalDate deadline){
        this.deadline = deadline;
    }
    public User getAssignedUser(){
        return assignedUser;
    }
    public void setAssignedUser(User assignedUser){
        this.assignedUser = assignedUser;
    }
    public Project getProject(){
        return project;
    }
    public void setProject(Project project){
        this.project = project;
    }

}