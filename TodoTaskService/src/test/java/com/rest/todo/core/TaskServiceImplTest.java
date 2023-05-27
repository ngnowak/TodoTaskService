package com.rest.todo.core;

import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.task.TaskRepository;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.infrastructure.repository.user.UserJPA;
import com.rest.todo.infrastructure.repository.user.UserRepository;
import com.rest.todo.core.task.TaskServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;
    private final Random random = new Random();

    @Test
    void returnTasksWithGivenTodoStatus() {
        // given
        var status = TodoStatus.COMPLETED;
        var taskName = "taskName";
        Mockito.when(taskRepository.findTasksByTodoStatusAndTaskName(taskName, status)).thenReturn(randomTasks());

        // when
        var result = taskService.getTasks(taskName, status);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    private List<TaskJPA> randomTasks() {
        return Lists.newArrayList(
               TaskJPA.builder()
                       .id(random.nextLong())
                       .description("desc1")
                       .taskName("title1")
                       .todoStatus(TodoStatus.values()[random.nextInt(2)])
                       .user(randomUser())
                       .build(),
                TaskJPA.builder()
                        .id(random.nextLong())
                        .description("desc2")
                        .taskName("title2")
                        .todoStatus(TodoStatus.values()[random.nextInt(2)])
                        .user(randomUser())
                        .build()
        );
    }

    private UserJPA randomUser() {
        return UserJPA.builder()
                .id(random.nextLong())
                .name(random.nextInt() + " " + random.nextInt())
                .build();
    }
}
