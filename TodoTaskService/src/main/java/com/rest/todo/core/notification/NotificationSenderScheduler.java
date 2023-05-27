package com.rest.todo.core.notification;

import com.rest.todo.core.user.UserServiceImpl;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.rest.todo.core.notification.NotificationMessage.getMessageForUser;

@AllArgsConstructor
@Component
public class NotificationSenderScheduler {
    private final UserServiceImpl userService;
    private final Logger logger = LoggerFactory.getLogger(NotificationSenderScheduler.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Scheduled(fixedRate = 10000)
    public  void scheduleNotification() {
        var users = userService.getUserWithTasksWithStatus(TodoStatus.NOT_COMPLETED);
        users.forEach( user -> logger.info(getMessageForUser(user)));
    }
}
