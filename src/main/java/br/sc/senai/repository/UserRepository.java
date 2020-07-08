package br.sc.senai.repository;

import br.sc.senai.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByNameContaining(String name);

}
