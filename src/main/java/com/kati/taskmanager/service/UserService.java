package com.kati.taskmanager.service;

import com.kati.taskmanager.entity.User;
import com.kati.taskmanager.mapper.UserMapper;
import com.kati.taskmanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Constructor injection
    public UserService(UserRepository userRepository,
                       UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(existingUser);
    }
}