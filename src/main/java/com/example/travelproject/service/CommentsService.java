package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.domain.Users;
import com.example.travelproject.exception_resolver.NoAccessByIdExceptions;
import com.example.travelproject.repository.CommentRepository;
import com.example.travelproject.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    //try DTO
    public Comments addComments(String text, Users userDTO, Attractions attractions) {
        if (securityService.checkAccessById(userDTO.getId())) {
            Comments comments = new Comments();
            comments.setText(text);
            comments.setUsers(userDTO);
            comments.setAttractions(attractions);
            log.info(String.format("Comments add id: " + "User id: " + userDTO.getId()) + "Attractions id: " + attractions.getId());
            return commentRepository.save(comments);
        }
        throw new NoAccessByIdExceptions(userDTO.getId(), userDTO.getFirstName());
    }

    public boolean deleteCommentsById(Long id, Long userId) {
        try {
            Comments comments = commentRepository.getReferenceById(id);
            if (securityService.checkAccessById(userId) && Objects.equals(comments.getUsers().getId(), userId)) {
                commentRepository.deleteById(id);
                log.info(String.format("comments delete id: " + id + " User id: " + userId));
            } else {
                log.warn(String.format("Пользователь с id " + userId + " не имеет доступа для удаления комментария с id " + id));
                return false;
            }
        } catch (Exception e) {
            log.warn(String.format("Ошибка при удалении комментария с id " + id, e.getMessage()));
            return false;
        }
        return true;
    }
    public void deleteCommentsByIdAdmin(Long id){
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
