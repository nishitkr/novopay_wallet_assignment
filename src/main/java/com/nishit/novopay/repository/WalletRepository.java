package com.nishit.novopay.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.nishit.novopay.model.Wallet;

public interface WalletRepository extends CrudRepository<Wallet, UUID>{

}
