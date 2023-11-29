package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.domain.Users;
import com.example.travelproject.service.CommentsService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @GetMapping("/attractions/{id}")
    public ResponseEntity<Comments> getCommentsId(@PathVariable("id") Long id) {
        Optional<Comments> comments = commentsService.getCommentsId(id);
        if (comments.isPresent()) {
            return new ResponseEntity<>(comments.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("")
    public ResponseEntity<Comments> createComment(@RequestParam("text") String text,
                                                  @RequestParam("userId") Long userId,
                                                  @RequestParam("attractionId") Long attractionId) {
        Users users = new Users();
        users.setId(userId);
        Attractions attraction = new Attractions();
        attraction.setId(attractionId);
        Comments comment = commentsService.addComments(text, users, attraction);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id, @RequestParam Long userId) {
        commentsService.deleteCommentsById(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody Comments comments) {
        return new ResponseEntity<>(commentsService.updateComments(comments) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}

