package com.rest.todo.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.todo.core.notification.NotificationSenderExecutor;
import com.rest.todo.core.user.UserNotFoundException;
import com.rest.todo.core.user.UserService;
import com.rest.todo.infrastructure.repository.task.TaskRepository;
import com.rest.todo.infrastructure.repository.user.UserRepository;
import com.rest.todo.presentation.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static com.rest.todo.presentation.contract.UserContract.USERS_BASE_PATH;
import static com.rest.todo.presentation.contract.UserContract.USER_CONSUMES_MEDIA_TYPE;
import static com.rest.todo.presentation.model.TaskMapper.toJpa;
import static com.rest.todo.utils.UserUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    NotificationSenderExecutor notificationSenderExecutor;

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void verifyCreateValidUserRequest() throws Exception {
        // given
        var user = toUser(defaultUserName, defaultPhoneNumber);
        var createdUser = UserDTO.builder()
                .id(getRandomId())
                .name(defaultUserName)
                .phoneNumber(defaultPhoneNumber)
                .build();

        when(userService.createUser(toJpa(user))).thenReturn(toJpa(createdUser));

        // when/ then
        var response = mockMvc.perform(post(USERS_BASE_PATH)
                        .content(objectMapper.writeValueAsBytes(user))
                .contentType(USER_CONSUMES_MEDIA_TYPE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(createdUser));
    }

    @Test
    void verifyCreatedUserRequestWithInvalidUserName() throws Exception {
        var user = toUser(invalidUserName, defaultPhoneNumber);

        mockMvc.perform(post(USERS_BASE_PATH)
                .content(objectMapper.writeValueAsBytes(user))
                .contentType(USER_CONSUMES_MEDIA_TYPE)).andExpect(status().isBadRequest());
    }

    @Test
    void verifyCreateUserRequestWithInvalidPhoneNumber() throws Exception {
        var user = toUser(defaultUserName, invalidPhoneNumber);

        mockMvc.perform(post(USERS_BASE_PATH)
                .content(objectMapper.writeValueAsBytes(user))
                .contentType(USER_CONSUMES_MEDIA_TYPE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyUpdateInvalidUserRequest() throws Exception {
        var user = toUser(invalidUserName, defaultPhoneNumber);
        mockMvc.perform(put(USERS_BASE_PATH + "/" + 1)
                .content(objectMapper.writeValueAsBytes(user))
                .contentType(USER_CONSUMES_MEDIA_TYPE)).andExpect(status().isBadRequest());
    }

    @Test
    void verifyUpdateUserRequest() throws Exception {
        //given
        var userId = getRandomId();
        var updatedUser = toUser(defaultUserName, defaultPhoneNumber);
        when(userService.updateUser(userId, toJpa(updatedUser))).thenReturn(toJpa(updatedUser));

        // when/ then
        var response = mockMvc.perform(put(USERS_BASE_PATH + "/" + userId)
                .content(objectMapper.writeValueAsBytes(updatedUser))
                .contentType(USER_CONSUMES_MEDIA_TYPE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(updatedUser));
    }

    @Test
    void verifyGetUserRequest() throws Exception {
        // given
        var userId = getRandomId();
        var user = toUser(userId, defaultUserName, defaultPhoneNumber);
        when(userService.getUser(userId)).thenReturn(toJpa(user));

        var response = mockMvc.perform(get(USERS_BASE_PATH + "/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(user));
    }

    @Test
    void verifyGetUserRequestForNotExistingUser() throws Exception {
        // given
        var userId = getRandomId();
        when(userService.getUser(userId)).thenThrow(new UserNotFoundException(userId));

        // when
        var response = mockMvc.perform(get(USERS_BASE_PATH + "/" + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo("User with id: " + userId + " does not exist");
    }
}
