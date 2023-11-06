package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.UsersRepository;
import com.example.travelproject.service.UsersService;
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
@RequestMapping("/users")
public class UsersController {


    private final UsersService usersService;
    private final UsersRepository usersRepository;

    public UsersController(UsersService usersService, UsersRepository usersRepository) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
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
    public ResponseEntity<HttpStatus> addFavoriteAttractions(@RequestParam Long userId,@RequestBody Long attractionsId){
        usersService.addFavoriteCountry(userId,attractionsId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/{userId}/favoriteAttractions")
    public ResponseEntity<Set<Attractions>> getAllFavoriteAttractions(@PathVariable Long userId){
        Users users = usersRepository.findById(userId).orElse(null);
        if (users==null){
            return ResponseEntity.notFound().build();
        }
        Set<Attractions> favoriteAttractions = users.getFavoriteAttractions();
        return ResponseEntity.ok(favoriteAttractions);
    }

}
