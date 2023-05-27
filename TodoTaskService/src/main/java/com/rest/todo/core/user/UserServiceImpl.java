package com.rest.todo.core.user;

import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.infrastructure.repository.user.UserJPA;

import java.util.Collection;

public interface UserServiceImpl {
    UserJPA createUser(UserJPA user);
    UserJPA updateUser(Long userId, UserJPA user);
    UserJPA getUser(Long userId);
    Collection<UserJPA> getUserWithTasksWithStatus(TodoStatus todoStatus);
}
