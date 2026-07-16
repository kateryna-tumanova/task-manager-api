package com.kati.taskmanager.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "users") // Protection against reserved word "user" in PostgreSQL

public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment (1, 2, 3...) in DB
    private Long id;

    @Column(nullable = false, length = 100) // Field name cannot be null
    private String name;

    @Column(nullable = false, unique = true) // Email cannot be null and must be unique
    private String email;

    // 1. No-args constructor for Hibernate (required for reflection)
    public User(){
    }

    // 2. All-args constructor (for convenient object creation in our code)
    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    // 3. Getters and Setters (so other layers can read and modify the data)
    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

}
