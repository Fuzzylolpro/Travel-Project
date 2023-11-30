package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.UsersRepository;
import com.example.travelproject.security.service.SecurityService;
import com.example.travelproject.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.util.Set;

@RestController
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final SecurityService securityService;

    public UsersController(UsersService usersService, UsersRepository usersRepository, SecurityService securityService) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.securityService = securityService;
    }


    @GetMapping
    public ResponseEntity<List<Users>> getAll() {
        List<Users> usersList = usersService.getAll();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUsersId(@PathVariable("id") Long id) {
        Optional<Users> users = usersService.getUsersId(id);
        if (users.isPresent()) {
            return new ResponseEntity<>(users.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody Users users) {
        return new ResponseEntity<>(usersService.createUsers(users) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        usersService.deleteUsersById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody Users users) {
        return new ResponseEntity<>(usersService.updateUsers(users) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PostMapping("/addFavoriteAttractions")
    public ResponseEntity<String> addFavoriteAttractions(@RequestParam Long userId, @RequestParam Long attractionsId) {
        boolean isAdded = usersService.addFavoriteAttractions(userId, attractionsId);
        if (isAdded) {
            return ResponseEntity.ok("Attractions added to favorites");
        } else {
            return ResponseEntity.badRequest().body("Error adding attractions to favorites");
        }
    }

    @DeleteMapping("/deleteFavoriteAttractions")
    public ResponseEntity<String> deleteFavoriteAttractions(@RequestParam Long userId, @RequestParam Long attractionsId) {
        boolean isDeleted = usersService.deleteFavoriteAttractions(userId, attractionsId);
        if (isDeleted) {
            return ResponseEntity.ok("Attractions deleted to favorites");
        } else {
            return ResponseEntity.badRequest().body("Error deleted attractions to favorites");
        }
    }

    @GetMapping("/{userId}/favoriteAttractions")
    public ResponseEntity<Set<Attractions>> getAllFavoriteAttractionsByUser(@PathVariable Long userId) {
        Optional<Users> users = usersRepository.findById(userId);
        if (users.isPresent() && securityService.checkAccessById(userId)) {
            Set<Attractions> favoriteAttractions = users.orElseThrow().getFavoriteAttractions();
            return ResponseEntity.ok(favoriteAttractions);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
