package com.mahmouddiane.taskmanager.dto.request;

import com.mahmouddiane.taskmanager.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
}
