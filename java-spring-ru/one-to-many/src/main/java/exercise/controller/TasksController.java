package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> index() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map)
                .toList();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        return taskMapper.map(task);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody @Valid TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        var dev = userRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + dto.getAssigneeId() + " not found"));
        taskRepository.save(task);
        dev.addTask(task);

        return taskMapper.map(task);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@RequestBody @Valid TaskUpdateDTO dto, @PathVariable long id) {
        var task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(dto, task);

        var assignee = userRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + dto.getAssigneeId() + " not found"));

        if (task.getAssignee().getId() != assignee.getId()) {

            task.getAssignee().getTasks().remove(task);

            task.setAssignee(assignee);
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            taskRepository.save(task);

            assignee.addTask(task);

            return taskMapper.map(task);
        } else {

            task.getAssignee().getTasks().remove(task);

            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            taskRepository.save(task);
            task.getAssignee().addTask(task);

            return taskMapper.map(task);
        }

    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        var assignee = task.getAssignee();
        assignee.getTasks().remove(task);

        taskRepository.delete(task);
    }


    // END
}
