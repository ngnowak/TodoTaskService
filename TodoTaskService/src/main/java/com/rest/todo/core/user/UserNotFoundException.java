package com.rest.todo.core.user;

import com.rest.todo.core.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super("User with id: " + id + " does not exist");
    }
}
