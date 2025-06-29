package com.work.task.config;

import com.work.task.service.TaskService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TaskServiceTestConfig {

  @Bean
  public TaskService taskService() {
    return Mockito.mock(TaskService.class);
  }
}
