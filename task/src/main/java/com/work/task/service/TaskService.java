package com.work.task.service;

import com.work.task.controller.dto.TaskRequest;
import com.work.task.controller.dto.TaskResponse;
import com.work.task.exception.TaskNotFoundException;
import com.work.task.model.Task;
import com.work.task.model.TaskStatus;
import com.work.task.repository.TaskRepository;
import com.work.task.specification.TaskSpecifications;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository repository;

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").withZone(java.time.ZoneId.systemDefault());

  public List<TaskResponse> findAllByUser(UUID userId) {
    return findAllByUserWithFilterAndSort(userId, null, null, "createdAt", "asc");
  }

  public TaskResponse findByIdForUser(Long id, UUID userId) {
    Task task =
        repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
    return toResponse(task);
  }

  public List<TaskResponse> findAllByUserWithFilterAndSort(
      UUID userId, String status, String title, String sortBy, String order) {

    if (sortBy == null || sortBy.isBlank()) {
      sortBy = "createdAt";
    }

    Specification<Task> spec =
        TaskSpecifications.userIdEquals(userId)
            .and(TaskSpecifications.statusEquals(status))
            .and(TaskSpecifications.titleContains(title));

    Sort.Direction direction =
        "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort sort = Sort.by(direction, sortBy);

    List<Task> tasks = repository.findAll(spec, sort);

    return tasks.stream().map(this::toResponse).collect(Collectors.toList());
  }

  public TaskResponse createTask(TaskRequest taskRequest, UUID userId) {
    Task task = new Task();
    task.setUserId(userId);
    task.setTitle(taskRequest.getTitle());
    task.setDescription(taskRequest.getDescription());

    if (taskRequest.getDueDate() != null && !taskRequest.getDueDate().isBlank()) {
      task.setDueDate(Instant.from(FORMATTER.parse(taskRequest.getDueDate())));
    }

    task.setStatus(taskRequest.getStatus() == null ? TaskStatus.NEW : taskRequest.getStatus());

    task = repository.save(task);
    return toResponse(task);
  }

  public TaskResponse updateTask(Long id, TaskRequest taskRequest, UUID userId) {
    Task task =
        repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));

    task.setTitle(taskRequest.getTitle());
    task.setDescription(taskRequest.getDescription());

    if (taskRequest.getDueDate() != null && !taskRequest.getDueDate().isBlank()) {
      task.setDueDate(Instant.from(FORMATTER.parse(taskRequest.getDueDate())));
    }

    if (taskRequest.getStatus() != null) {
      task.setStatus(taskRequest.getStatus());
    }

    task = repository.save(task);
    return toResponse(task);
  }

  public void deleteTask(Long id, UUID userId) {
    Task task =
        repository
            .findByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
    repository.delete(task);
  }

  private TaskResponse toResponse(Task task) {
    return TaskResponse.builder()
        .id(task.getId())
        .title(task.getTitle())
        .description(task.getDescription())
        .createdAt(FORMATTER.format(task.getCreatedAt()))
        .updatedAt(task.getUpdatedAt() != null ? FORMATTER.format(task.getUpdatedAt()) : null)
        .status(task.getStatus())
        .dueDate(task.getDueDate() != null ? FORMATTER.format(task.getDueDate()) : null)
        .build();
  }
}
