package com.example.travelproject.service;

import com.example.travelproject.domain.City;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentsService {
    private final CommentRepository commentRepository;

    public CommentsService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public List<Comments> getAll() {
        return commentRepository.findAll();
    }

    public Optional<Comments> getCommentsId(long id) {
        return commentRepository.findById(id);
    }

    public Boolean createComments(Comments comments) {
        try {
            commentRepository.save(comments);
            log.info(String.format("comments created " + comments.getText()));
        } catch (Exception e) {
            log.warn(String.format("error", comments.getText()));
            return false;
        }
        return true;
    }

    public void deleteCommentsById(Long id) {
        commentRepository.deleteById(id);
    }

    public Boolean updateComments(Comments comments) {
        try {
            commentRepository.saveAndFlush(comments);
            log.info(String.format("comments update id: " + comments.getId()));
        } catch (Exception e) {
            log.warn(String.format("error", comments.getId(), e));
            return false;
        }
        return true;
    }
}

