package com.nishit.novopay.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.nishit.novopay.model.UserCredential;

public interface UserCredentialRepository extends CrudRepository<UserCredential, UUID>{
	public Optional<UserCredential> findByUsernameAndPassword(String username, String password);
}
