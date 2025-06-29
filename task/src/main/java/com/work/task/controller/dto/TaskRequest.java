package com.work.task.controller.dto;

import com.work.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

  @NotBlank(message = "Title обязателен")
  @Size(max = 255, message = "Title должен быть не длиннее 255 символов")
  private String title;

  private String description;

  private String dueDate;

  private TaskStatus status;
}
