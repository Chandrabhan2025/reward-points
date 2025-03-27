package com.reward.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reward.dto.RewardDto;
import com.reward.entity.Transactions;
import com.reward.exception.CustomerNotFoundException;
import com.reward.service.TransactionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {
	
	@Autowired
	private TransactionsService transactionsService;
	
	@PostMapping("/add")
	public ResponseEntity<String> addTransaction(@Valid @RequestBody Transactions transactions){
		if(transactions.getAmount()<=0) {
			return new ResponseEntity<String>(transactionsService.addTransaction(transactions), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(transactionsService.addTransaction(transactions), HttpStatus.OK);
	}
	
	@GetMapping("/get/{customerId}")
	public ResponseEntity<List<Transactions>> getTransactionsById(@PathVariable long customerId) throws CustomerNotFoundException{
		List<Transactions> transactionsList = transactionsService.getByCustomerId(customerId);
		return new ResponseEntity<List<Transactions>>(transactionsList, HttpStatus.OK);
		
	}
	
	@GetMapping("/getByMonth/{customerId}")
	public ResponseEntity<Map<String, Long>> getRewardsByMonth(@PathVariable long customerId) throws CustomerNotFoundException{
		Map<String, Long> res = transactionsService.getRewardPointsByMonth(customerId);
		return new ResponseEntity<Map<String,Long>>(res, HttpStatus.OK);
		
	}
	
	@GetMapping("/getByDate/{customerId}/{start}/{end}")
	public ResponseEntity<Long> getRewardsBetweenDate(@PathVariable long customerId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) throws CustomerNotFoundException{
		Long rewardPoint = transactionsService.getRewardPoints(customerId, start, end);
		return new ResponseEntity<Long>(rewardPoint, HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<RewardDto>> getAll(){
		return new ResponseEntity<List<RewardDto>>(transactionsService.getAllCustomerRewards(), HttpStatus.OK) ;
	}
	
	@GetMapping("/getRewardsByParticularMonth/{customerId}/{month}/{year}")
	public ResponseEntity<RewardDto> getRewardsByParticularMonth(@PathVariable long customerId, @PathVariable int month, @PathVariable int year) throws CustomerNotFoundException{
		return new ResponseEntity<RewardDto>(transactionsService.getRewardsByParticularMonth(customerId, month, year), HttpStatus.OK);
	}
}
