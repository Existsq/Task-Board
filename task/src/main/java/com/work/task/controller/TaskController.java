package com.work.task.controller;

import com.work.task.controller.dto.TaskRequest;
import com.work.task.controller.dto.TaskResponse;
import com.work.task.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping
  public List<TaskResponse> findAll(
      @RequestHeader("X-User-Id") UUID userId,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String order) {
    if ((status == null || status.isBlank())
        && (title == null || title.isBlank())
        && (sortBy == null || sortBy.isBlank())
        && (order == null || order.isBlank())) {
      return taskService.findAllByUser(userId);
    }

    return taskService.findAllByUserWithFilterAndSort(userId, status, title, sortBy, order);
  }

  @GetMapping("/{id}")
  public TaskResponse findById(@PathVariable Long id, @RequestHeader("X-User-Id") UUID userId) {
    return taskService.findByIdForUser(id, userId);
  }

  @PostMapping
  public TaskResponse create(
      @RequestBody @Valid TaskRequest task, @RequestHeader("X-User-Id") UUID userId) {
    return taskService.createTask(task, userId);
  }

  @PutMapping("/{id}")
  public TaskResponse update(
      @PathVariable Long id,
      @RequestBody @Valid TaskRequest task,
      @RequestHeader("X-User-Id") UUID userId) {
    return taskService.updateTask(id, task, userId);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id, @RequestHeader("X-User-Id") UUID userId) {
    taskService.deleteTask(id, userId);
  }
}
