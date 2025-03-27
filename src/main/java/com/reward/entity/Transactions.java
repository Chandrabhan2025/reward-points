package com.reward.entity;

import java.time.LocalDate;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long transactionId;
	
	@NotNull(message = "Customer Id cannot be Empty...")
	@Min(value = 1, message = "Customer Id should be 1 or greater than 1")
	private long customerId;
	
	@NotNull(message = "Amount cannot be empty")
	@Positive(message = "Amount should be positive")
	private double amount;
	
	private long rewardPoints;
	
	@PastOrPresent(message = "Transacation date must be in present or past")
	private LocalDate date;

}