package com.work.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.work.task.controller.dto.TaskRequest;
import com.work.task.controller.dto.TaskResponse;
import com.work.task.exception.TaskNotFoundException;
import com.work.task.model.Task;
import com.work.task.model.TaskStatus;
import com.work.task.repository.TaskRepository;
import com.work.task.service.TaskService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class TaskServiceTest {

  @Mock private TaskRepository repository;

  @InjectMocks private TaskService service;

  private UUID userId;

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    userId = UUID.randomUUID();
  }

  @Test
  void findAllByUser_shouldReturnTasks() {
    Task task = createSampleTask();
    when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(task));

    List<TaskResponse> result = service.findAllByUser(userId);

    assertEquals(1, result.size());
    assertEquals(task.getTitle(), result.get(0).getTitle());
  }

  @Test
  void findByIdForUser_taskExists_shouldReturnTask() {
    Task task = createSampleTask();
    when(repository.findByIdAndUserId(task.getId(), userId)).thenReturn(Optional.of(task));

    TaskResponse response = service.findByIdForUser(task.getId(), userId);

    assertNotNull(response);
    assertEquals(task.getId(), response.getId());
    // Проверим, что даты в формате строки
    assertNotNull(response.getCreatedAt());
    assertNotNull(response.getDueDate());
  }

  @Test
  void findByIdForUser_taskNotFound_shouldThrow() {
    when(repository.findByIdAndUserId(anyLong(), any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> service.findByIdForUser(1L, userId));
  }

  @Test
  void createTask_shouldSaveAndReturn() {
    TaskRequest request = new TaskRequest();
    request.setTitle("Test");
    request.setDescription("Desc");
    request.setStatus(TaskStatus.NEW);
    request.setDueDate(FORMATTER.format(ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));

    Task savedTask = createSampleTask();
    savedTask.setTitle(request.getTitle());
    when(repository.save(any(Task.class))).thenReturn(savedTask);

    TaskResponse response = service.createTask(request, userId);

    assertNotNull(response);
    assertEquals(request.getTitle(), response.getTitle());
  }

  @Test
  void updateTask_taskExists_shouldUpdateAndReturn() {
    TaskRequest request = new TaskRequest();
    request.setTitle("Updated");
    request.setDescription("Updated desc");
    request.setStatus(TaskStatus.IN_PROGRESS);
    request.setDueDate(FORMATTER.format(ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));

    Task existingTask = createSampleTask();
    when(repository.findByIdAndUserId(existingTask.getId(), userId))
        .thenReturn(Optional.of(existingTask));
    when(repository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

    TaskResponse response = service.updateTask(existingTask.getId(), request, userId);

    assertEquals(request.getTitle(), response.getTitle());
    assertEquals(request.getStatus(), response.getStatus());
  }

  @Test
  void updateTask_taskNotFound_shouldThrow() {
    when(repository.findByIdAndUserId(anyLong(), any(UUID.class))).thenReturn(Optional.empty());

    TaskRequest request = new TaskRequest();
    assertThrows(TaskNotFoundException.class, () -> service.updateTask(1L, request, userId));
  }

  @Test
  void deleteTask_taskExists_shouldDelete() {
    Task task = createSampleTask();
    when(repository.findByIdAndUserId(task.getId(), userId)).thenReturn(Optional.of(task));
    doNothing().when(repository).delete(task);

    assertDoesNotThrow(() -> service.deleteTask(task.getId(), userId));
    verify(repository).delete(task);
  }

  @Test
  void deleteTask_taskNotFound_shouldThrow() {
    when(repository.findByIdAndUserId(anyLong(), any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> service.deleteTask(1L, userId));
  }

  private Task createSampleTask() {
    Task task = new Task();
    task.setId(1L);
    task.setUserId(userId);
    task.setTitle("Title");
    task.setDescription("Description");
    task.setStatus(TaskStatus.NEW);
    task.setCreatedAt(Instant.now());
    task.setUpdatedAt(Instant.now());
    task.setDueDate(Instant.now().plusSeconds(3600));
    return task;
  }
}
