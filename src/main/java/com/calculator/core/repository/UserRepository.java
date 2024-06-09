package com.calculator.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.calculator.data.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    public Optional<User> findByUsername(String username);
}
