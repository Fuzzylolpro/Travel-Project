package com.example.travelproject.repository;

import com.example.travelproject.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comments,Long> {
    List<Comments> findAllByUsers_Id (Long userId);


}
