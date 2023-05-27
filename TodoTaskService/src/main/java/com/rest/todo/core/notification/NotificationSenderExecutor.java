package com.rest.todo.core.notification;

import com.rest.todo.core.user.UserServiceImpl;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.rest.todo.core.notification.NotificationMessage.getMessageForUser;

@Component
public class NotificationSenderExecutor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final Logger logger = LoggerFactory.getLogger(NotificationSenderExecutor.class);
    private final UserServiceImpl userService;

    @Autowired
    public NotificationSenderExecutor(UserServiceImpl userService) {
        this.userService = userService;
    }

    public void notifyAboutNotCompletedTasks() throws ExecutionException, InterruptedException {
          Future<?> future = executorService.submit( () -> {
                        var users = userService.getUserWithTasksWithStatus(TodoStatus.NOT_COMPLETED);
                        users.forEach( user -> logger.info(getMessageForUser(user)));
                        return "The message was send to: " + users.size() + " users";
                  }
          );

          logger.info("ExecutorService finished with message: " + future.get());
    }
}
