package exercise.controller;

import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

import org.junit.jupiter.api.BeforeEach;

// END
@SpringBootTest
@AutoConfigureMockMvc
// BEGIN
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    private Task testTask;

    @BeforeEach
    public void sepUp() {
        testTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
        taskRepository.save(testTask);
    }

    @Test
    public void testShow() throws Exception {
        var mvcResult = mockMvc.perform(get("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        String title = testTask.getTitle();
        String description = testTask.getDescription();
        assertThatJson(contentAsString).and(
                a -> a.node("title").isEqualTo(title),
                a -> a.node("description").isEqualTo(description)
        );
    }

    @Test
    public void testCreate() throws Exception {
        int countElementInDB = (int) taskRepository.count();
        Task newTestTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
        String title = newTestTask.getTitle();
        String description = newTestTask.getDescription();
        assertThat(taskRepository.findByTitle(title)).isEmpty();

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTestTask));

        mockMvc.perform(request)
                .andExpect(status().isCreated());


        Task task = taskRepository.findByTitle(title).get();
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);

        assertThat(taskRepository.findAll()).hasSize(countElementInDB + 1);
    }

    @Test
    public void testUpdate() throws Exception {
        long id = testTask.getId();

        HashMap<Object, Object> data = new HashMap<>();
        String newTitleTask = "English";
        String newDescriptionTask = "I need to take English class tomorrow!";
        data.put("title", newTitleTask);
        data.put("description", newDescriptionTask);

        var request = put("/tasks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        Task task = taskRepository.findById(id).get();
        assertThat(task.getTitle()).isEqualTo(newTitleTask);
        assertThat(task.getDescription()).isEqualTo(newDescriptionTask);
    }

    @Test
    public void testDelete() throws Exception {
        long id = testTask.getId();

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isOk());

        assertThat(taskRepository.findById(id)).isEmpty();
    }
    // END
}
