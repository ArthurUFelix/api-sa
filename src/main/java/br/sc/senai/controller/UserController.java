package br.sc.senai.controller;

import br.sc.senai.exception.UserNotFoundException;
import br.sc.senai.model.User;
import br.sc.senai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);

        return "Usuário criado com sucesso";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/update")
    public @ResponseBody
    String updateUser(@RequestParam Integer id, @RequestParam String name, @RequestParam String email, @RequestParam String password) {
        try {
            User user = userRepository.findById(id).get();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }

        return "Usuário atualizado com sucesso";
    }

    @PostMapping(path = "/remove")
    public @ResponseBody
    String removeUser(@RequestParam Integer id) {
        userRepository.deleteById(id);

        return "Usuário excluído com sucesso";
    }

    @PostMapping(path = "/allbyname")
    public @ResponseBody
    Iterable<User> findByName(@RequestParam String name) {
        return userRepository.findByNameContaining(name);
    }
}
