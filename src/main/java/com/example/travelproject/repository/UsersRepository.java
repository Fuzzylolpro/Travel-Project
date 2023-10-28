package com.example.travelproject.repository;

import com.example.travelproject.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    @Override
    <S extends Users> S save(S entity);
}
