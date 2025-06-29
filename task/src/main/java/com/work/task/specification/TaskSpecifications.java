package com.work.task.specification;

import com.work.task.model.Task;
import com.work.task.model.TaskStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

  public static Specification<Task> userIdEquals(java.util.UUID userId) {
    return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (userId == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("userId"), userId);
    };
  }

  public static Specification<Task> statusEquals(String statusStr) {
    return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (statusStr == null || statusStr.isBlank()) {
        return cb.conjunction();
      }
      try {
        TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
        return cb.equal(root.get("status"), status);
      } catch (IllegalArgumentException e) {
        return cb.disjunction();
      }
    };
  }

  public static Specification<Task> titleContains(String title) {
    return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (title == null || title.isBlank()) {
        return cb.conjunction();
      }
      return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    };
  }
}
