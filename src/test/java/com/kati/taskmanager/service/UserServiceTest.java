package com.kati.taskmanager.service;

import com.kati.taskmanager.dto.user.UserCreateRequest;
import com.kati.taskmanager.dto.user.UserResponse;
import com.kati.taskmanager.dto.user.UserUpdateRequest;
import com.kati.taskmanager.entity.User;
import com.kati.taskmanager.exception.UserAlreadyExistsException;
import com.kati.taskmanager.exception.UserNotFoundException;
import com.kati.taskmanager.mapper.UserMapper;
import com.kati.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnAllUsersSuccessfully() {

        User user1 =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        User user2 =
                new User(
                        "Maria",
                        "maria@example.com"
                );

        UserResponse response1 =
                new UserResponse(
                        1L,
                        "Anna",
                        "anna@example.com"
                );

        UserResponse response2 =
                new UserResponse(
                        2L,
                        "Maria",
                        "maria@example.com"
                );

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        when(userMapper.toResponse(user1))
                .thenReturn(response1);

        when(userMapper.toResponse(user2))
                .thenReturn(response2);

        List<UserResponse> result =
                userService.getAllUsers();

        assertEquals(
                List.of(response1, response2),
                result
        );

        verify(userRepository).findAll();

        verify(userMapper).toResponse(user1);

        verify(userMapper).toResponse(user2);
    }

    @Test
    void getUserById_shouldReturnUserWhenUserExists() {

        Long id = 1L;

        User user = new User("Anna", "anna@example.com");

        UserResponse response =
                new UserResponse(1L, "Anna", "anna@example.com");

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        when(userMapper.toResponse(user))
                .thenReturn(response);

        UserResponse result =
                userService.getUserById(id);

        assertEquals(response, result);

        verify(userRepository).findById(id);

        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserDoesNotExist() {

        Long id = 1L;

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(id)
        );

        verify(userRepository).findById(id);

        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void createUser_shouldCreateUserSuccessfully() {

        UserCreateRequest request =
                new UserCreateRequest("Anna", "anna@example.com");

        User user = new User("Anna", "anna@example.com");

        User savedUser = new User("Anna", "anna@example.com");

        UserResponse response =
                new UserResponse(1L, "Anna", "anna@example.com");

        when(userRepository.existsByEmailIgnoreCase(request.email()))
                .thenReturn(false);

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(savedUser);

        when(userMapper.toResponse(savedUser))
                .thenReturn(response);

        UserResponse result = userService.createUser(request);

        assertEquals(response, result);

        verify(userRepository).existsByEmailIgnoreCase(request.email());

        verify(userMapper).toEntity(request);

        verify(userRepository).save(user);

        verify(userMapper).toResponse(savedUser);
    }

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {

        UserCreateRequest request =
                new UserCreateRequest("Anna", "anna@example.com");

        when(userRepository.existsByEmailIgnoreCase(request.email()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(request)
        );

        verify(userRepository)
                .existsByEmailIgnoreCase(request.email());

        verify(userMapper, never()).toEntity(any());

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateUserSuccessfully() {

        Long id = 1L;

        UserUpdateRequest request =
                new UserUpdateRequest(
                        "Anna Updated",
                        "anna.updated@example.com"
                );

        User existingUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        User savedUser =
                new User(
                        "Anna Updated",
                        "anna.updated@example.com"
                );

        UserResponse response =
                new UserResponse(
                        1L,
                        "Anna Updated",
                        "anna.updated@example.com"
                );

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmailIgnoreCaseAndIdNot(
                request.email(),
                id
        ))
                .thenReturn(false);

        when(userRepository.save(existingUser))
                .thenReturn(savedUser);

        when(userMapper.toResponse(savedUser))
                .thenReturn(response);

        UserResponse result =
                userService.updateUser(id, request);

        assertEquals(response, result);

        verify(userRepository).findById(id);

        verify(userRepository)
                .existsByEmailIgnoreCaseAndIdNot(
                        request.email(),
                        id
                );

        verify(userMapper).updateEntity(
                request,
                existingUser
        );

        verify(userRepository).save(existingUser);

        verify(userMapper).toResponse(savedUser);
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserDoesNotExist() {

        Long id = 1L;

        UserUpdateRequest request =
                new UserUpdateRequest(
                        "Anna Updated",
                        "anna.updated@example.com"
                );

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(id, request)
        );

        verify(userRepository).findById(id);

        verify(userRepository, never())
                .existsByEmailIgnoreCaseAndIdNot(any(), any());

        verify(userMapper, never())
                .updateEntity(
                        any(UserUpdateRequest.class),
                        any(User.class)
                );

        verify(userRepository, never()).save(any());

        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenEmailAlreadyExists() {

        Long id = 1L;

        UserUpdateRequest request =
                new UserUpdateRequest(
                        "Anna Updated",
                        "anna.updated@example.com"
                );

        User existingUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmailIgnoreCaseAndIdNot(
                request.email(),
                id
        ))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.updateUser(id, request)
        );

        verify(userRepository).findById(id);

        verify(userRepository).existsByEmailIgnoreCaseAndIdNot(
                request.email(),
                id);

        verify(userMapper, never())
                .updateEntity(
                        any(UserUpdateRequest.class),
                        any(User.class)
                );

        verify(userRepository, never()).save(any());

        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void deleteUser_shouldDeleteUserSuccessfully() {

        Long id = 1L;

        User existingUser =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        when(userRepository.findById(id))
                .thenReturn(Optional.of(existingUser));

        userService.deleteUser(id);

        verify(userRepository).findById(id);

        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteUser_shouldThrowExceptionWhenUserDoesNotExist() {

        Long id = 1L;

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(id)
        );

        verify(userRepository).findById(id);

        verify(userRepository, never()).delete(any());
    }
}
