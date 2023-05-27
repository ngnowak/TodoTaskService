package com.rest.todo.presentation;

import com.rest.todo.core.notification.NotificationSenderExecutor;
import com.rest.todo.core.user.UserServiceImpl;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.presentation.model.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.rest.todo.presentation.contract.TaskContract.ID_PATH_PARAM;
import static com.rest.todo.presentation.contract.TaskContract.STATUS_QUERY_PARAM;
import static com.rest.todo.presentation.contract.UserContract.*;
import static com.rest.todo.presentation.model.TaskMapper.toDTO;
import static com.rest.todo.presentation.model.TaskMapper.toJpa;

@AllArgsConstructor
@RestController
@RequestMapping(value = USERS_BASE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserServiceImpl userService;
    private final NotificationSenderExecutor notificationSenderExecutor;

    @PostMapping(consumes = USER_CONSUMES_MEDIA_TYPE)
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        var user = userService.createUser(toJpa(userDTO));
        return ResponseEntity.ok(toDTO(user));
    }

    @PutMapping(value = "/{" + ID_PATH_PARAM + "}", consumes = USER_CONSUMES_MEDIA_TYPE)
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable(ID_PATH_PARAM) Long userId,
            @RequestBody @Valid UserDTO userDTO) {
        var user = userService.updateUser(userId, toJpa(userDTO));
        return ResponseEntity.ok(toDTO(user));
    }

    @GetMapping("/{" + ID_PATH_PARAM + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(ID_PATH_PARAM) Long userId) {
        var user = userService.getUser(userId);
        return ResponseEntity.ok(toDTO(user));
    }

    @GetMapping(GET_TASKS_PATH)
    public ResponseEntity<Collection<UserDTO>> getUsersWithNotCompletedTasks(
            @RequestParam(STATUS_QUERY_PARAM) TodoStatus status) {
        var users = userService.getUserWithTasksWithStatus(status).stream()
                .map(user -> toDTO(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = NOTIFY_PATH, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> notifyUsersAboutNotCompletedTasks() {
        try {
            notificationSenderExecutor.notifyAboutNotCompletedTasks();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Exception occurred while sending notification: " + e.getMessage());
        }

        return ResponseEntity.accepted().build();
    }
}
