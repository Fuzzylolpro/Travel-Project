package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Users;
import com.example.travelproject.exception_resolver.NoAccessByIdExceptions;
import com.example.travelproject.repository.AttractionRepository;
import com.example.travelproject.repository.UsersRepository;
import com.example.travelproject.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;
    private final AttractionRepository attractionRepository;
    private final SecurityService securityService;

    public UsersService(UsersRepository usersRepository, AttractionRepository attractionRepository, SecurityService securityService) {
        this.usersRepository = usersRepository;
        this.attractionRepository = attractionRepository;
        this.securityService = securityService;
    }

    public List<Users> getAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUsersId(long id) {
        if (securityService.checkAccessById(id)) {
            return usersRepository.findById(id);
        }
        throw new NoAccessByIdExceptions(id, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public Boolean createUsers(Users users) {
        try {
            usersRepository.save(users);
            log.info(String.format("users created " + users.getFirstName() + " " + users.getSecondName()));
        } catch (Exception e) {
            log.warn(String.format("error", users.getFirstName()));
            return false;
        }
        return true;
    }

    public void deleteUsersById(Long id) {
        try {
            if (securityService.checkAccessById(id)) {
                usersRepository.deleteById(id);
                log.info(String.format("users delete id: " + id));
            }
        } catch (Exception e) {
            log.warn(String.format("error ", id, e));
        }
    }

    public Boolean updateUsers(Users users) {
        try {
            Long userId = users.getId();
            if (securityService.checkAccessById(userId)) {
                usersRepository.saveAndFlush(users);
                log.info(String.format("users update id: " + users.getId()));
            }
        } catch (Exception e) {
            log.warn(String.format("error ", users.getId(), e));
            return false;
        }
        return true;
    }

    public boolean addFavoriteAttractions(Long userId, Long attractionsId) {
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);
            Optional<Attractions> attractionsOptional = attractionRepository.findById(attractionsId);
            if (userOptional.isPresent() && attractionsOptional.isPresent() && securityService.checkAccessById(userId)) {
                Users user = userOptional.get();
                Attractions attractions = attractionsOptional.get();
                user.getFavoriteAttractions().add(attractions);
                usersRepository.save(user);
                log.info(String.format("users " + userId + " add favorite attractions " + attractionsId));
            } else {
                if (userOptional.isEmpty()) {
                    log.warn(String.format("Пользователь с id " + userId + " не найден"));
                    return false;
                }
                if (attractionsOptional.isEmpty()) {
                    log.warn(String.format("Достопримечательность с id " + attractionsId + " не найдена"));
                    return false;
                }
                log.warn(String.format("Ошибка при добавлении достопримечательности в избранное для пользователя " + userId));
                return false;
            }
        } catch (Exception e) {
            log.warn(String.format("Error", userId, e));
            return false;
        }
        return true;
    }


    public boolean deleteFavoriteAttractions(Long userId, Long attractionsId) {
        try {
            Optional<Users> userOptional = usersRepository.findById(userId);
            Optional<Attractions> attractionsOptional = attractionRepository.findById(attractionsId);

            if (userOptional.isPresent() && attractionsOptional.isPresent() && securityService.checkAccessById(userId)) {
                Users user = userOptional.get();
                Attractions attractions = attractionsOptional.get();

                if (user.getFavoriteAttractions().contains(attractions)) {
                    user.getFavoriteAttractions().remove(attractions);
                    usersRepository.save(user);
                    log.info(String.format("User " + userId + "has removed favorite attraction " + attractionsId));
                } else {
                    log.warn(String.format("User " + userId + "does not have attraction " + attractionsId + " as a favorite"));
                    return false;
                }
            } else {
                log.warn(String.format("User " + userId + " or attraction " + attractionsId + " not found"));
            return false;
            }
        } catch (Exception e) {
            log.error(String.format("Error deleting favorite attraction for user " + userId, e));
            return false;
        }
        return true;
    }
}

