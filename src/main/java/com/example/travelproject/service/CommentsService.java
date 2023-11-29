package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.CommentRepository;
import com.example.travelproject.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentsService {
    private final CommentRepository commentRepository;
    private final SecurityService securityService;

    public CommentsService(CommentRepository commentRepository, SecurityService securityService) {
        this.commentRepository = commentRepository;

        this.securityService = securityService;
    }


    public List<Comments> getAll() {
        return commentRepository.findAll();
    }

    public Optional<Comments> getCommentsId(long id) {
        return commentRepository.findById(id);
    }

    public Comments addComments(String text, Users users, Attractions attractions) {
        Comments comments = new Comments();
        comments.setText(text);
        comments.setUsers(users);
        comments.setAttractions(attractions);
        return commentRepository.save(comments);
    }

    public void deleteCommentsById(Long id, Long userId) {
        try {
            Comments comments = commentRepository.getReferenceById(id);
            if (comments.getUsers().getId().equals(userId) || securityService.checkAccessById(userId)) {
                commentRepository.deleteById(id);
            }
            log.info(String.format("comments delete id: " + comments.getId()));
        } catch (Exception e) {
            log.warn(String.format("error", id, e));
        }
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
