package com.rest.todo.presentation.model;

import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.user.UserJPA;

import java.util.Optional;

public class TaskMapper {

    public static TaskDTO toDTO(TaskJPA taskJPA) {
        return TaskDTO.builder()
                .id(taskJPA.getId())
                .title(taskJPA.getTaskName())
                .description(taskJPA.getDescription())
                .todoStatus(taskJPA.getTodoStatus())
                .dataCreated(taskJPA.getDataCreated())
                .lastModified(taskJPA.getLastModified())
                .user(toDTO(taskJPA.getUser()))
                .build();
    }

    public static UserDTO toDTO(UserJPA userJPA) {
        return UserDTO.builder()
                .id(userJPA.getId())
                .name(userJPA.getName())
                .phoneNumber(userJPA.getPhoneNumber())
                .build();
    }

    public static TaskJPA toJpa(TaskDTO taskDTO) {
        return TaskJPA.builder()
                .taskName(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .todoStatus(taskDTO.getTodoStatus())
                .dataCreated(taskDTO.getDataCreated())
                .lastModified(taskDTO.getLastModified())
                .user(toJpa(taskDTO.getUser()))
                .build();
    }

    public static UserJPA toJpa(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        var user = UserJPA.builder()
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber());

        Optional.ofNullable(userDTO.getId()).ifPresent(userId -> user.id(userId));
        return user.build();
    }

}
