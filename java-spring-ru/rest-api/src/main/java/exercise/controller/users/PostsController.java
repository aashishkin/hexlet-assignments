package exercise.controller.users;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    public Optional<List<Post>> show(@PathVariable int id) {
        var postsList = posts.stream()
                .filter(p -> p.getUserId() == id)
                .toList();
        return Optional.of(postsList);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> create(@PathVariable int id, @RequestBody Post post) {
        post.setUserId(id);
        posts.add(post);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }


}
// END
