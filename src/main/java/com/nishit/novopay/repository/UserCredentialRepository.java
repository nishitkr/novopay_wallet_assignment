package com.nishit.novopay.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.nishit.novopay.model.UserCredential;

public interface UserCredentialRepository extends CrudRepository<UserCredential, UUID>{

}
