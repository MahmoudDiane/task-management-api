package com.mahmouddiane.taskmanager.service;

import com.mahmouddiane.taskmanager.dto.request.CreateTaskRequest;
import com.mahmouddiane.taskmanager.dto.request.UpdateTaskRequest;
import com.mahmouddiane.taskmanager.dto.response.TaskResponse;
import com.mahmouddiane.taskmanager.exception.ForbiddenException;
import com.mahmouddiane.taskmanager.exception.ResourceNotFoundException;
import com.mahmouddiane.taskmanager.model.Task;
import com.mahmouddiane.taskmanager.model.User;
import com.mahmouddiane.taskmanager.model.enums.TaskStatus;
import com.mahmouddiane.taskmanager.repository.TaskRepository;
import com.mahmouddiane.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    @Transactional
    public TaskResponse createTask(Long userId, CreateTaskRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .user(user)
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return taskRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse updateTask(Long userId, Long taskId, UpdateTaskRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        if (!task.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You don't own this task");
        }

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());

        return mapToResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long userId, Long taskId) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        if (!task.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You don't own this task");
        }
        taskRepository.delete(task);
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
