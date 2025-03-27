package com.reward.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reward.entity.Transactions;

@Repository
public interface TransactionsRepository extends  JpaRepository<Transactions, Long> {
	
	List<Transactions> findByCustomerId(Long customerId);
	List<Transactions> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);

}