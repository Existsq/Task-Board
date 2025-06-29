package com.work.task.controller.dto;

import com.work.task.model.TaskStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

  private Long id;

  private String title;

  private String description;

  private String createdAt;

  private String updatedAt;

  private TaskStatus status;

  private String dueDate;
}
