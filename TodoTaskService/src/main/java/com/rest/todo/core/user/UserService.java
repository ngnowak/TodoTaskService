package com.rest.todo.core.user;

import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.infrastructure.repository.user.UserJPA;
import com.rest.todo.infrastructure.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;

    @Override
    public UserJPA createUser(UserJPA user) {
        return userRepository.save(user);
    }

    @Override
    public UserJPA updateUser(Long userId, UserJPA user) {
        return userRepository.findById(userId)
                .map(userJPA -> {
                    userJPA.setName(user.getName());
                    return userRepository.save(userJPA);
                })
                .orElseGet(() -> userRepository.save(user));
    }

    @Override
    public UserJPA getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    @Transactional
    public Collection<UserJPA> getUserWithTasksWithStatus(TodoStatus todoStatus) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(user -> user.getTasks().stream().anyMatch(task -> task.getTodoStatus() == todoStatus))
                .collect(Collectors.toList());
    }
}
