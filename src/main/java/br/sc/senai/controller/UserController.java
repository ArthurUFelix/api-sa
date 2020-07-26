package br.sc.senai.controller;

import br.sc.senai.model.User;
import br.sc.senai.repository.UserRepository;
import br.sc.senai.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@Import(WebSecurityConfig.class)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/users")
    public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers() {
        try {
            Iterable<User> users = userRepository.findAll();
            if(((Collection<?>) users).size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/users/{id}")
    public @ResponseBody ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);

        if(userData.isPresent()) {
            try {
                User updatedUser = userData.get();
                updatedUser.setName(user.getName());
                updatedUser.setEmail(user.getEmail());
                updatedUser.setPassword(user.getPassword());
                return new ResponseEntity<>(userRepository.save(updatedUser), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/users/{id}")
    public @ResponseBody ResponseEntity<HttpStatus> removeUser(@PathVariable("id") Integer id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(path = "/users/name/{name}")
    public @ResponseBody ResponseEntity<Iterable<User>> findByName(@PathVariable("name") String name) {
        try {
            Iterable<User> users = userRepository.findByNameContaining(name);
            if(((Collection<?>) users).size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
