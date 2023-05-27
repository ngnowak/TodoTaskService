package com.rest.todo.presentation.contract;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskContract {
    public static final String TASKS_BASE_PATH = "/api/tasks";
    public static final String GET_TASKS_PATH = "/tasks";

    public static final String STATUS_QUERY_PARAM = "status";
    public static final String NAME_QUERY_PARAM = "name";
    public static final String ID_PATH_PARAM = "id";

    public static final String TODO_TASK_CONSUMES_MEDIA_TYPE = "application/vnd.todo.list.task.v1+json";

}
