package com.rest.todo.presentation.contract;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContract {
    public static final String USERS_BASE_PATH = "/api/users";
    public static final String GET_TASKS_PATH = "/tasks";
    public static final String NOTIFY_PATH = "/notify";

    public static final String USER_CONSUMES_MEDIA_TYPE = "application/vnd.todo.list.user.v1+json";

}
