package br.sc.senai.controller;

import br.sc.senai.model.Transaction;
import br.sc.senai.model.User;
import br.sc.senai.repository.TransactionRepository;
import br.sc.senai.repository.UserRepository;
import br.sc.senai.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Import(WebSecurityConfig.class)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private TransactionRepository transactionRepository;

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
    public @ResponseBody ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);

        if(userData.isPresent()) {
            try {
                User updatedUser = userData.get();
                updatedUser.setName(user.getName());
                updatedUser.setEmail(user.getEmail());

                if(user.getPassword() != null) {
                    updatedUser.setPassword(encoder.encode(user.getPassword()));
                }

                Optional<User> existsUser = userRepository.findByEmail(user.getEmail());

                if(existsUser.isPresent() && existsUser.get().getId() != updatedUser.getId() ) {
                    return new ResponseEntity<>("O email inserido já está em uso", HttpStatus.BAD_REQUEST);
                }

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

    @GetMapping(path = "/users/{id}/transactions")
    public @ResponseBody ResponseEntity<?> getTransactionsByUserId(
            @PathVariable("id") Integer id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "999") Integer size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc(id, paging);

            if(transactions.getSize() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("transactions", transactions.getContent());
            response.put("currentPage", transactions.getNumber());
            response.put("totalItems", transactions.getTotalElements());
            response.put("totalPages", transactions.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/users/{id}/transactions/{transactionId}")
    public @ResponseBody ResponseEntity<?> getUserTransactionById(
            @PathVariable("id") Integer id,
            @PathVariable("transactionId") Integer transactionId) {
        try {
            Optional<Transaction> transaction = transactionRepository.findByIdAndUserId(transactionId, id);

            if(transaction.isPresent()) {
                return new ResponseEntity<>(transaction, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
