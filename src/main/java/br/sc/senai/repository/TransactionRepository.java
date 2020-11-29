package br.sc.senai.repository;

import br.sc.senai.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserId(Integer id);

    Page<Transaction> findByUserIdOrderByCreatedAtDesc(Integer id, Pageable pageable);
}
