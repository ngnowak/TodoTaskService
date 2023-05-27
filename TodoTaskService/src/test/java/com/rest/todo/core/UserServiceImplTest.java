package com.rest.todo.core;

import com.rest.todo.core.user.UserService;
import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.infrastructure.repository.user.UserJPA;
import com.rest.todo.infrastructure.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(userRepository);
    }

    @Test
    void shouldGetUserWithNotCompletedTasksReturnsOnlyUserWithNotCompletedTasks() {
        // given
        var users = List.of(
                new UserJPA(1L, "1", "", List.of(notCompletedTask(1L), completedTask(2L))),
                new UserJPA(1L, "1", "", List.of(completedTask(3L)))
        );
        var expectedUsers = List.of(
                new UserJPA(1L, "1", "", List.of(notCompletedTask(1L), completedTask(2L)))
        );
        when(userRepository.findAll()).thenReturn(users);

        // when
        var result = userService.getUserWithTasksWithStatus(TodoStatus.NOT_COMPLETED);

        // then
        assertThat(result).isEqualTo(expectedUsers);
    }


    @Test
    void shouldGetUserWithCompletedTasksReturnsOnlyUserWithCompletedTasks() {
        // given
        var users = List.of(
                new UserJPA(1L, "1", "", List.of(notCompletedTask(1L), completedTask(2L))),
                new UserJPA(2L, "2", "", List.of(completedTask(3L))),
                new UserJPA(3L, "3", "", List.of(notCompletedTask(4L)))
        );
        var expectedUsers =List.of(
                new UserJPA(1L, "1", "", List.of(notCompletedTask(1L), completedTask(2L))),
                new UserJPA(2L, "2", "", List.of(completedTask(3L)))
        );
        when(userRepository.findAll()).thenReturn(users);

        // when
        var result = userService.getUserWithTasksWithStatus(TodoStatus.COMPLETED);

        // then
        assertThat(result).isEqualTo(expectedUsers);
    }


    private TaskJPA notCompletedTask(Long id) {
        return TaskJPA.builder()
                .id(id)
                .todoStatus(TodoStatus.NOT_COMPLETED)
                .build();
    }

    private TaskJPA completedTask(Long id) {
        return TaskJPA.builder()
                .id(id)
                .todoStatus(TodoStatus.COMPLETED)
                .build();
    }
}
