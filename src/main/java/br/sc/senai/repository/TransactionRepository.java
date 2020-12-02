package br.sc.senai.repository;

import br.sc.senai.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserId(Integer id);

    Optional<Transaction> findByIdAndUserId(Integer id, Integer userId);

    Page<Transaction> findByUserIdOrderByCreatedAtDesc(Integer id, Pageable pageable);
}
