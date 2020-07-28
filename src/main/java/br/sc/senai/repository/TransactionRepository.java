package br.sc.senai.repository;

import br.sc.senai.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    List<Transaction> findByUserId(Integer id);
}
