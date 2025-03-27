package com.reward.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.reward.dto.RewardDto;
import com.reward.entity.Transactions;
import com.reward.exception.CustomerNotFoundException;

public interface TransactionsService {
	
	String addTransaction(Transactions transactions);
	List<Transactions> getByCustomerId(long customerId) throws CustomerNotFoundException;
	List<Transactions> getByCutomerIdBetweenDate(long customerId, LocalDate startDate, LocalDate endDate) throws CustomerNotFoundException;
	Long getRewardPoints(long customerId, LocalDate startDate, LocalDate endDate) throws CustomerNotFoundException;
	Map<String, Long> getRewardPointsByMonth(long customerId) throws CustomerNotFoundException;
	List<RewardDto> getAllCustomerRewards();
	RewardDto getRewardsByParticularMonth(long customerId, int month, int year);
	
}