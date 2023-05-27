package com.rest.todo.presentation;

import com.rest.todo.presentation.model.TaskDTO;
import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.core.task.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rest.todo.presentation.contract.TaskContract.*;
import static com.rest.todo.presentation.model.TaskMapper.toJpa;

@RestController
@RequestMapping(value = TASKS_BASE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskServiceImpl taskService;

    @Autowired
    TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        var tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{" + ID_PATH_PARAM + "}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable(ID_PATH_PARAM) Long taskId) {
        var task = taskService.getTask(taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/by")
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(value = NAME_QUERY_PARAM, required = false) String taskName,
            @RequestParam(STATUS_QUERY_PARAM) TodoStatus todoStatus) {
        var task = taskService.getTasks(taskName, todoStatus);
        return ResponseEntity.ok(task);
    }
    @PostMapping(consumes = TODO_TASK_CONSUMES_MEDIA_TYPE)
    public ResponseEntity<TaskJPA> createTask(@RequestBody @Validated TaskDTO task) {
        var savedTask = taskService.create(toJpa(task));
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping(value = "/{" + ID_PATH_PARAM + "}", consumes = TODO_TASK_CONSUMES_MEDIA_TYPE)
    public ResponseEntity<?> updateTask(
            @PathVariable(ID_PATH_PARAM) Long taskId,
            @RequestBody @Validated TaskDTO task) {
        taskService.updateTask(taskId, toJpa(task));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{" + ID_PATH_PARAM + "}/complete")
    public ResponseEntity<?> completeTask(@PathVariable(ID_PATH_PARAM) Long taskId) {
        taskService.completeTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{" + ID_PATH_PARAM + "}")
    public ResponseEntity<?> deleteTask(@PathVariable(ID_PATH_PARAM) Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
