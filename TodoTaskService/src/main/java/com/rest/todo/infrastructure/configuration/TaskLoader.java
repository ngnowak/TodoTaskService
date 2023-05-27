package com.rest.todo.infrastructure.configuration;

import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.task.TaskRepository;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.infrastructure.repository.user.UserJPA;
import com.rest.todo.infrastructure.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TaskLoader implements CommandLineRunner {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(TaskLoader.class);

    @Autowired
    TaskLoader(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadTodos();
    }

    private void loadTodos() {
        if (taskRepository.count() == 0) {
            var user = userRepository.save(
                    UserJPA.builder()
                            .name("Anna Kowalska")
                            .phoneNumber("+48123456789")
                            .build()
            );

            taskRepository.save(
                    TaskJPA.builder()
                            .taskName("Go to market")
                            .description("Buy eggs from market")
                            .todoStatus(TodoStatus.NOT_COMPLETED)
                            .user(user)
                            .build()
            );
            taskRepository.save(
                    TaskJPA.builder()
                            .taskName("Go to school")
                            .description("Complete assignments")
                            .todoStatus(TodoStatus.NOT_COMPLETED)
                            .user(user)
                            .build()
            );

            logger.info("Sample Todos Loaded");
        }
    }
}
