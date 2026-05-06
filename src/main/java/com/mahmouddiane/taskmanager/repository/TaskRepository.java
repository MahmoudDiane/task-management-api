package com.mahmouddiane.taskmanager.repository;

import com.mahmouddiane.taskmanager.model.Task;
import com.mahmouddiane.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
