package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.domain.DTO.UserDTO;
import com.example.travelproject.domain.Users;
import com.example.travelproject.service.CommentsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/comments")
public class CommentsController {
    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public ResponseEntity<List<Comments>> getAll() {
        List<Comments> commentsList = commentsService.getAll();
        return new ResponseEntity<>(commentsList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentsId(@PathVariable("id") Long id) {
        Optional<Comments> comments = commentsService.getCommentsId(id);
        if (comments.isPresent()) {
            return new ResponseEntity<>(comments.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<Comments>> getAllById(@PathVariable("id") Long id) {
        List<Comments> commentsList = commentsService.getCommentsByUserId(id);
        return new ResponseEntity<>(commentsList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Comments> createComment(@RequestParam("text") String text,
                                                  @RequestBody UserDTO userDTO,
                                                  @RequestParam("attractionId") Long attractionId) {
        Users users = new Users();
        users.setId(userDTO.getId());
        Attractions attraction = new Attractions();
        attraction.setId(attractionId);
        Comments comment = commentsService.addComments(text, users, attraction);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long id, @RequestParam Long userId) {
        if (commentsService.deleteCommentsById(id, userId)) {
            return ResponseEntity.ok("Comment deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error deleting comment");
        }
    }


    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody Comments comments) {
        return new ResponseEntity<>(commentsService.updateComments(comments) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}

