package br.sc.senai.controller;

import br.sc.senai.model.Transaction;
import br.sc.senai.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping(path = "/transactions")
    public @ResponseBody ResponseEntity<Transaction> addNewTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction newTransaction = transactionRepository.save(transaction);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(path = "/transactions")
    public @ResponseBody ResponseEntity<Iterable<Transaction>> getAllTransactions() {
        try {
            Iterable<Transaction> transactions = transactionRepository.findAll();
            if(((Collection<?>) transactions).size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/transactions/{id}")
    public @ResponseBody ResponseEntity<Transaction> updateTransaction(@PathVariable("id") Integer id, @RequestBody Transaction transaction) {
        Optional<Transaction> transactionData = transactionRepository.findById(id);

        if(transactionData.isPresent()) {
            try {
                Transaction updatedTransaction = transactionData.get();
                updatedTransaction.setDescription(transaction.getDescription());
                updatedTransaction.setFlow(transaction.getFlow());
                updatedTransaction.setValue(transaction.getValue());
                return new ResponseEntity<>(transactionRepository.save(updatedTransaction), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/transactions/{id}")
    public @ResponseBody ResponseEntity<HttpStatus> removeTransaction(@PathVariable("id") Integer id) {
        try {
            transactionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
