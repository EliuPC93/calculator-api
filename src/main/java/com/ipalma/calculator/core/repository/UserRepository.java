package com.ipalma.calculator.core.repository;

import com.ipalma.calculator.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public Optional<User> findByEmail(String email);
}
