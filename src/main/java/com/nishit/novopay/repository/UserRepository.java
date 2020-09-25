package com.nishit.novopay.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nishit.novopay.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>{
	public Optional<User> findByUsername(String username);
}
