package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public List<Users> getAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUsersId(long id) {
        return usersRepository.findById(id);
    }

    public Boolean createUsers(Users users) {
        try {
            usersRepository.save(users);
            log.info(String.format("users created " + users.getFirstName() +" "+ users.getSecondName()));
        } catch (Exception e) {
            log.warn(String.format("error", users.getFirstName()));
            return false;
        }
        return true;
    }

    public void deleteUsersById(Long id) {
        usersRepository.deleteById(id);
    }

    public Boolean updateUsers(Users users) {
        try {
            usersRepository.saveAndFlush(users);
            log.info(String.format("users update id: " + users.getId()));
        } catch (Exception e) {
            log.warn(String.format("error", users.getId(), e));
            return false;
        }
        return true;
    }
}

