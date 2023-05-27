package com.rest.todo.utils;


import com.rest.todo.presentation.model.UserDTO;

import java.util.Random;

public class UserUtils {

    public static final String defaultUserName = "Jan Kowalski";
    public static final String invalidUserName = "Invalid";
    public static final String defaultPhoneNumber = "+48999999999";
    public static final String invalidPhoneNumber = "999999999";

    private static final Random random = new Random();

    private UserUtils() {

    }

    public static Long getRandomId() {
        return random.nextLong();
    }

    public static UserDTO toUser(String userName, String phoneNumber) {
        return UserDTO.builder()
                .name(userName)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static UserDTO toUser(Long userId, String userName, String phoneNumber) {
        return UserDTO.builder()
                .id(userId)
                .name(userName)
                .phoneNumber(phoneNumber)
                .build();
    }
}
