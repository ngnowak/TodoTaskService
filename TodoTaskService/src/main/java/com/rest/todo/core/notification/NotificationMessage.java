package com.rest.todo.core.notification;

import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.user.UserJPA;

import java.util.stream.Collectors;

public class NotificationMessage {

    static String getMessageForUser(UserJPA user) {
        return "User " + user.getName() + " has not completed tasks: " +
                user.getTasks().stream().map(TaskJPA::getTaskName)
                        .collect(Collectors.joining(", ", "[", "]"));
    }
}
