package com.rest.todo.core.task;

import com.rest.todo.presentation.model.TaskDTO;
import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.task.TaskRepository;
import com.rest.todo.infrastructure.repository.task.TodoStatus;
import com.rest.todo.infrastructure.repository.user.UserJPA;
import com.rest.todo.infrastructure.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rest.todo.presentation.model.TaskMapper.toDTO;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskDTO> getTasks() {
        List<TaskDTO> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(
                task -> tasks.add(toDTO(task))
        );
        return tasks;
    }

    @Override
    public TaskDTO getTask(Long id) {
        return toDTO(
                taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id)));
    }

    @Override
    public List<TaskDTO> getTasks(String name, TodoStatus todoStatus) {
        List<TaskDTO> tasks = new ArrayList<>();
        taskRepository.findTasksByTodoStatusAndTaskName(name, todoStatus)
                .forEach(task -> tasks.add(toDTO(task)));
        return tasks;
    }

    @Override
    public TaskJPA create(TaskJPA task) {
        UserJPA user = Optional.ofNullable(task.getUser().getId())
                .flatMap(userRepository::findById)
                        .orElseGet(() -> userRepository.save(task.getUser()));

        task.setUser(user);
        return taskRepository.save(task);
    }

    @Override
    public void updateTask(Long id, TaskJPA task) {
        TaskJPA existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setTaskName(task.getTaskName());
        existingTask.setDescription(task.getDescription());
        existingTask.setTodoStatus(task.getTodoStatus());

        taskRepository.save(existingTask);
    }

    @Override
    public void completeTask(Long id) {
        TaskJPA existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        existingTask.setTodoStatus(TodoStatus.COMPLETED);
        taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
