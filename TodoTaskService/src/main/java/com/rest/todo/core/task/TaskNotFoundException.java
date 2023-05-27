package com.rest.todo.core.task;

import com.rest.todo.core.NotFoundException;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task with id: " + id + " does not exist");
    }
}

