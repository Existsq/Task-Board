package com.work.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.task.config.TaskServiceTestConfig;
import com.work.task.controller.TaskController;
import com.work.task.controller.dto.TaskRequest;
import com.work.task.controller.dto.TaskResponse;
import com.work.task.model.TaskStatus;
import com.work.task.service.TaskService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
@Import(TaskServiceTestConfig.class)
public class TaskControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired
  private TaskService taskService;

  @Autowired private ObjectMapper objectMapper;

  private UUID userId;
  private TaskResponse sampleResponse;
  private TaskRequest sampleRequest;

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").withZone(ZoneId.systemDefault());

  @BeforeEach
  void setup() {
    userId = UUID.randomUUID();

    Instant now = Instant.now();
    Instant due = now.plusSeconds(3600);

    sampleResponse =
        TaskResponse.builder()
            .id(1L)
            .title("Test Task")
            .description("Description")
            .createdAt(FORMATTER.format(now))
            .updatedAt(FORMATTER.format(now))
            .status(TaskStatus.NEW)
            .dueDate(FORMATTER.format(due))
            .build();

    sampleRequest =
        TaskRequest.builder()
            .title("Test Task")
            .description("Description")
            .status(TaskStatus.NEW)
            .dueDate(FORMATTER.format(due))
            .build();
  }

  @Test
  void testFindAllWithoutFilters_callsFindAllByUser() throws Exception {
    Mockito.when(taskService.findAllByUser(userId)).thenReturn(List.of(sampleResponse));

    mockMvc
        .perform(get("/api/v1/tasks").header("X-User-Id", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(sampleResponse.getId()))
        .andExpect(jsonPath("$[0].title").value(sampleResponse.getTitle()));

    Mockito.verify(taskService).findAllByUser(userId);
  }

  @Test
  void testFindAllWithFilters_callsFindAllByUserWithFilterAndSort() throws Exception {
    Mockito.when(
            taskService.findAllByUserWithFilterAndSort(
                eq(userId), eq("NEW"), eq("Test"), eq("title"), eq("desc")))
        .thenReturn(List.of(sampleResponse));

    mockMvc
        .perform(
            get("/api/v1/tasks")
                .header("X-User-Id", userId.toString())
                .param("status", "NEW")
                .param("title", "Test")
                .param("sortBy", "title")
                .param("order", "desc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(sampleResponse.getId()))
        .andExpect(jsonPath("$[0].title").value(sampleResponse.getTitle()));

    Mockito.verify(taskService, Mockito.never()).findAllByUser(any());
    Mockito.verify(taskService)
        .findAllByUserWithFilterAndSort(userId, "NEW", "Test", "title", "desc");
  }

  @Test
  void testFindById() throws Exception {
    Mockito.when(taskService.findByIdForUser(1L, userId)).thenReturn(sampleResponse);

    mockMvc
        .perform(get("/api/v1/tasks/1").header("X-User-Id", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
        .andExpect(jsonPath("$.title").value(sampleResponse.getTitle()));

    Mockito.verify(taskService).findByIdForUser(1L, userId);
  }

  @Test
  void testCreateTask() throws Exception {
    Mockito.when(taskService.createTask(any(TaskRequest.class), eq(userId)))
        .thenReturn(sampleResponse);

    mockMvc
        .perform(
            post("/api/v1/tasks")
                .header("X-User-Id", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
        .andExpect(jsonPath("$.title").value(sampleResponse.getTitle()));

    Mockito.verify(taskService).createTask(any(TaskRequest.class), eq(userId));
  }

  @Test
  void testUpdateTask() throws Exception {
    Mockito.when(taskService.updateTask(eq(1L), any(TaskRequest.class), eq(userId)))
        .thenReturn(sampleResponse);

    mockMvc
        .perform(
            put("/api/v1/tasks/1")
                .header("X-User-Id", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(sampleResponse.getId()))
        .andExpect(jsonPath("$.title").value(sampleResponse.getTitle()));

    Mockito.verify(taskService).updateTask(eq(1L), any(TaskRequest.class), eq(userId));
  }

  @Test
  void testDeleteTask() throws Exception {
    Mockito.doNothing().when(taskService).deleteTask(1L, userId);

    mockMvc
        .perform(delete("/api/v1/tasks/1").header("X-User-Id", userId.toString()))
        .andExpect(status().isNoContent());

    Mockito.verify(taskService).deleteTask(1L, userId);
  }
}
