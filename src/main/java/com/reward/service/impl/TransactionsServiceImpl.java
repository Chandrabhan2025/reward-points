package com.reward.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reward.dto.RewardDto;
import com.reward.entity.Transactions;
import com.reward.exception.CustomerNotFoundException;
import com.reward.repository.TransactionsRepository;
import com.reward.service.TransactionsService;

@Service
public class TransactionsServiceImpl implements TransactionsService{
	
	@Autowired
	private TransactionsRepository transactionsRepository;
	
	//to add the transaction to database
	@Override
	public String addTransaction(Transactions transactions) {
		// TODO Auto-generated method stub
		if(transactions.getAmount() > 0) {
			int tempAmount = (int) transactions.getAmount();
			if(tempAmount > 100) {
				long points = (long) (((tempAmount - 100) * 2) + 50);
				transactions.setRewardPoints(points);
				transactionsRepository.save(transactions);
				return "Transaction is successfully added.";
			}
			else if(tempAmount>50 && tempAmount<=100) {
				long points = (long) (tempAmount - 50);
				transactions.setRewardPoints(points);
				transactionsRepository.save(transactions);
				return "Transaction is successfully added.";	
			}else {
				transactions.setRewardPoints(0);
				transactionsRepository.save(transactions);
				return "Transaction is successfully added.";
			}
		}
		return "Failed to add the transaction.";
	}

	//To get the transaction by customer id.
	@Override
	public List<Transactions> getByCustomerId(long customerId) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		if(!transactionsRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Please Enter Valid Customer ID");
		}
		return transactionsRepository.findByCustomerId(customerId);
	}

	//To get the transaction between date and customer id.
	@Override
	public List<Transactions> getByCutomerIdBetweenDate(long customerId, LocalDate startDate, LocalDate endDate) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		if(!transactionsRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Please Enter Valid Customer ID");
		}
		LocalDate currentDate = LocalDate.now();
		if(startDate.isAfter(currentDate) && endDate.isAfter(currentDate)) {
			throw new IllegalArgumentException("Start and end date should not be in future.");
		}
		
		if(startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date cannot be after end date");
		}
		return transactionsRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
	}

	//To get the reward points of particular customer by id and between date
	@Override
	public Long getRewardPoints(long customerId, LocalDate startDate, LocalDate endDate) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		if(!transactionsRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Please Enter Valid Customer ID");
		}
		LocalDate currentDate = LocalDate.now();
		if(startDate.isAfter(currentDate) && endDate.isAfter(currentDate)) {
			throw new IllegalArgumentException("Start and end date should not be in future.");
		}
		
		if(startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date cannot be after end date");
		}
		List<Transactions> transactionsList = transactionsRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
		Long rewardPoints = transactionsList.stream().mapToLong(Transactions::getRewardPoints).sum();
		return rewardPoints;
	}

	//To get all month reward points of particular customer
	@Override
	public Map<String, Long> getRewardPointsByMonth(long customerId) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		
		if(!transactionsRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Please Enter Valid Customer ID");
		}
		LocalDate now = LocalDate.now();
		Map<String, Long> rewardPerMonth = new LinkedHashMap<String, Long>();
		
		for(int i=0; i<3; i++) {
			YearMonth month = YearMonth.from(now.minusMonths(i));
			LocalDate start = month.atDay(1);
			LocalDate end = month.atEndOfMonth();
			
			List<Transactions> transactionsList = transactionsRepository.findByCustomerIdAndDateBetween(customerId, start, end);
			Long rewardPoints = transactionsList.stream().mapToLong(Transactions::getRewardPoints).sum();
			String monthName = month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + month.getYear();
			rewardPerMonth.put(monthName, rewardPoints);
		}
		return rewardPerMonth;
	}
	
	
	//To get all customer reward of past three month
	@Override
	public List<RewardDto> getAllCustomerRewards() {
		// TODO Auto-generated method stub
		List<Transactions> allTransactions = transactionsRepository.findAll();
		Map<Long, Long> rewardPointsMap = new HashMap<Long, Long>();
		Map<Long, Double> totalAmountMap = new HashMap<Long, Double>();
		List<RewardDto> rewardList = new ArrayList<RewardDto>();
		for(Transactions transactions: allTransactions) {
			long customerId = transactions.getCustomerId();
			rewardPointsMap.put(customerId, rewardPointsMap.getOrDefault(customerId, 0L)+transactions.getRewardPoints());
			totalAmountMap.put(customerId, totalAmountMap.getOrDefault(customerId, 0.0)+transactions.getAmount() );
		}
		
		for(Long customerId: rewardPointsMap.keySet()) {
			rewardList.add(new RewardDto(customerId, rewardPointsMap.get(customerId), totalAmountMap.get(customerId)));
		}
		
		return rewardList;
	}

	//To get rewards of particular customer by montH and year
	@Override
	public RewardDto getRewardsByParticularMonth(long customerId, int month, int year) {
		// TODO Auto-generated method stub
		YearMonth currentYearMonth = YearMonth.now();
		
		int current_year = currentYearMonth.getYear();
		int current_month = currentYearMonth.getMonthValue();
		if(month<1 || month>12) {
			throw new IllegalArgumentException("Month should be between 1 and 12");
		}
		
		if(year>current_year) {
			throw new IllegalArgumentException("Year should not be in future");
		}
		
		if(year==current_year && month>current_month) {
			throw new IllegalArgumentException("Future month of the current year are not allowed");
		}
		
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();
		List<Transactions> transactionsList = transactionsRepository.findByCustomerIdAndDateBetween(customerId, start, end);
		
		Long rewards = transactionsList.stream().mapToLong(Transactions::getRewardPoints).sum();
		double totalAmount = transactionsList.stream().mapToDouble(Transactions::getAmount).sum();
		
		
		return new RewardDto(customerId, rewards, totalAmount);
	}
	
}
