package com.nishit.novopay.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.nishit.novopay.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, UUID>{

}
