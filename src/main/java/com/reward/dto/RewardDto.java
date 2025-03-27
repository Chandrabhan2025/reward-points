package com.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardDto {
	
	private long customerId;
	private long rewardPoints;
	private double totalAmountSpent;
}