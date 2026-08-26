package com.kati.taskmanager.service;

import com.kati.taskmanager.dto.user.UserCreateRequest;
import com.kati.taskmanager.dto.user.UserResponse;
import com.kati.taskmanager.dto.user.UserUpdateRequest;
import com.kati.taskmanager.entity.User;
import com.kati.taskmanager.exception.UserAlreadyExistsException;
import com.kati.taskmanager.exception.UserNotFoundException;
import com.kati.taskmanager.mapper.UserMapper;
import com.kati.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Constructor injection
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new UserAlreadyExistsException();
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRepository.existsByEmailIgnoreCaseAndIdNot(
                request.email(),
                id
        )) {
            throw new UserAlreadyExistsException();
        }

        userMapper.updateEntity(request, existingUser);
        User savedUser = userRepository.save(existingUser);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(existingUser);
    }
}