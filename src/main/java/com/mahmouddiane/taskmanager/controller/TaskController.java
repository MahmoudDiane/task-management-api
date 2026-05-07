package com.mahmouddiane.taskmanager.controller;

import com.mahmouddiane.taskmanager.dto.request.CreateTaskRequest;
import com.mahmouddiane.taskmanager.dto.request.UpdateTaskRequest;
import com.mahmouddiane.taskmanager.dto.response.TaskResponse;
import com.mahmouddiane.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskResponse> getAllTasks(@RequestParam Long userId) {
        return taskService.getAllTasks(userId);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestParam Long userId, @RequestBody @Valid CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(userId, request));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@RequestParam Long userId, @PathVariable Long taskId, @RequestBody @Valid UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(userId, taskId, request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@RequestParam Long userId, @PathVariable Long taskId) {
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }
}
