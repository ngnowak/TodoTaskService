package com.rest.todo.presentation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.todo.presentation.model.TaskDTO;
import com.rest.todo.presentation.model.UserDTO;
import com.rest.todo.infrastructure.repository.task.TaskRepository;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.rest.todo.presentation.contract.TaskContract.TASKS_BASE_PATH;
import static com.rest.todo.presentation.contract.TaskContract.TODO_TASK_CONSUMES_MEDIA_TYPE;
import static com.rest.todo.presentation.model.TaskMapper.toJpa;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskController taskController;

    @Value(value = "${local.server.port}")
    private int port;

    private TestRestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @BeforeAll
    void init() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        restTemplate = new TestRestTemplate();
    }

    @Nested
    @DisplayName("Tests create method")
    class CreateTaskTest {

        private final String CREATE_TASK_PATH = "http://localhost:" + port + TASKS_BASE_PATH;

        @Test
        public void shouldReturnResponseOkForValidTaskWithUser() {
            // given
            var task = randomTask();
            var expectedTask = toJpa(task);
            expectedTask = expectedTask.builder().id(new Random().nextLong()).build();
            when(taskRepository.save(any())).thenReturn(expectedTask);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, TODO_TASK_CONSUMES_MEDIA_TYPE);
            HttpEntity<TaskDTO> taskRequest = new HttpEntity<>(task, httpHeaders);

            // when
            var request = restTemplate
                    .postForEntity(CREATE_TASK_PATH, taskRequest, TaskDTO.class);

            // then
            assertThat(request.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(request.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
            assertThat(request.getBody()).isNotNull();
            assertThat(request.getBody().id).isEqualTo(expectedTask.getId());
        }

        @Test
        public void shouldReturn415ForInvalidContentTypeWithUser() {
            // given
            var task = randomTask();
            var expectedTask = toJpa(task);
            when(taskRepository.save(any())).thenReturn(expectedTask);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<TaskDTO> taskRequest = new HttpEntity<>(task, httpHeaders);

            // when
            var request = restTemplate
                    .postForEntity(CREATE_TASK_PATH, taskRequest, TaskDTO.class);

            // then
            assertThat(request.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(415));
            assertThat(request.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        }

        @Test
        public void shouldReturnResponseBadRequestForInvalidTaskWithUser() {
            // given
            var task = TaskDTO.builder()
                    .title(null)
                    .description("desc")
                    .todoStatus(TodoStatus.COMPLETED)
                    .user(UserDTO.builder().name("name").build())
                    .build();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, TODO_TASK_CONSUMES_MEDIA_TYPE);
            HttpEntity<TaskDTO> taskRequest = new HttpEntity<>(task, httpHeaders);

            // when/ then
            var request = restTemplate
                    .postForEntity(CREATE_TASK_PATH, taskRequest, TaskDTO.class);

            // then
            assertThat(request.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(request.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        }

    }

    @Nested
    @DisplayName("Tests get by id")
    class GetTaskByIdTest {
        private final String GET_TASK_PATH = "http://localhost:" + port + TASKS_BASE_PATH;

        @Test
        public void shouldReturnResponseNotFoundForNotExistingTask() {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            var response = restTemplate.exchange(GET_TASK_PATH + "/" + 1, HttpMethod.GET, entity, String.class);

            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());

        }

        @Test
        public void shouldReturnResponseOkForGettingExistingTask() {
            // given
            var id = new Random().nextLong();
            var task = TaskDTO.builder()
                    .id(id)
                    .title("title")
                    .description("desc")
                    .todoStatus(TodoStatus.COMPLETED)
                    .user(UserDTO.builder().name("name").build())
                    .build();
            when(taskRepository.findById(id)).thenReturn(Optional.of(toJpa(task)));


            // when
            var request = restTemplate.getForEntity(GET_TASK_PATH + "/" + id, TaskDTO.class);

            // then
            assertThat(request.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
            assertThat(request.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        }
    }

    @Nested
    @DisplayName("Tests update")
    class UpdateTaskTest {
        private final String UPDATE_TASK_PATH = "http://localhost:" + port + TASKS_BASE_PATH;

        @Test
        void shouldUpdateTask() {
            var taskId = new Random().nextLong();
            var task = randomTask();
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(toJpa(task)));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, TODO_TASK_CONSUMES_MEDIA_TYPE);
            HttpEntity<TaskDTO> taskRequest = new HttpEntity<>(task, httpHeaders);

            // when
            var response = restTemplate.exchange(UPDATE_TASK_PATH + "/" + taskId, HttpMethod.PUT, taskRequest, TaskDTO.class);

            // then
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(response.getBody()).isNull();

        }

        @Test
        void shouldReturn404ForNotExistingTask() {
            var taskId = new Random().nextLong();
            var task = randomTask();
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, TODO_TASK_CONSUMES_MEDIA_TYPE);
            HttpEntity<TaskDTO> taskRequest = new HttpEntity<>(task, httpHeaders);

            // when
            var response = restTemplate.exchange(UPDATE_TASK_PATH + "/" + taskId, HttpMethod.PUT, taskRequest, String.class);

            // then
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(response.getBody()).isEqualTo("Task with id: " + taskId + " does not exist");

        }
    }

    @Nested
    @DisplayName("Tests remove")
    class RemoveTaskTest {
        private final String REMOVE_TASK_PATH = "http://localhost:" + port + TASKS_BASE_PATH;

        @Test
        void shouldRemoveTask() {
            var taskId = new Random().nextLong();
            var task = randomTask();
            doNothing().when(taskRepository).deleteById(taskId);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<TaskDTO> taskRequest = new HttpEntity<>(task, httpHeaders);

            // when
            var response = restTemplate.exchange(REMOVE_TASK_PATH + "/" + taskId, HttpMethod.DELETE,
                    taskRequest, Void.class);

            // then
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(response.getBody()).isNull();

        }
    }

    private TaskDTO randomTask() {
        return TaskDTO.builder()
                .title("title")
                .description("desc")
                .todoStatus(TodoStatus.COMPLETED)
                .user(UserDTO.builder().name("name").build())
                .build();
    }
}
