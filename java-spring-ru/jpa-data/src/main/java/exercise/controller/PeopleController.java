package exercise.controller;

import exercise.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Person;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping(path = "/{id}")
    public Person show(@PathVariable long id) {
        return personRepository.findById(id).get();
    }

    // BEGIN
    @GetMapping() // Список персон
    public List<Person> showAll() {
        return personRepository.findAll();
    }

    @PostMapping() // Добавление персоны
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        personRepository.save(person);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(person );
    }

    @DeleteMapping("/{id}") // Удаление персоны
    public void deletePerson(@PathVariable Long id) {
        personRepository.deleteById(id);
    }
    // END
}
