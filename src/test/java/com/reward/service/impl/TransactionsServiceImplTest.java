package com.reward.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reward.dto.RewardDto;
import com.reward.entity.Transactions;
import com.reward.exception.CustomerNotFoundException;
import com.reward.repository.TransactionsRepository;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceImplTest {
    
    @Mock
    private TransactionsRepository transactionsRepository;
    
    @InjectMocks
    private TransactionsServiceImpl transactionsService;
    
    private Transactions transaction1;
    private Transactions transaction2;
    
    @BeforeEach
    void setUp() {
        transaction1 = new Transactions(1L, 1L, 120.0, 90L, LocalDate.of(2025, 3, 1));
        transaction2 = new Transactions(2L, 1L, 80.0, 30L, LocalDate.of(2025, 3, 10));
    }
    
    @Test
    void testAddTransaction_Over100() {
        Transactions transaction = new Transactions(3L, 1L, 120.0, 0L, LocalDate.now());
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(transaction);
        
        String result = transactionsService.addTransaction(transaction);
        
        assertEquals("Transaction is successfully added.", result);
        assertEquals(90L, transaction.getRewardPoints());
    }
    
    @Test
    void testAddTransaction_Between50And100() {
        Transactions transaction = new Transactions(4L, 1L, 80.0, 0L, LocalDate.now());
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(transaction);
        
        String result = transactionsService.addTransaction(transaction);
        
        assertEquals("Transaction is successfully added.", result);
        assertEquals(30L, transaction.getRewardPoints());
    }
    
    @Test
    void testAddTransaction_Below50() {
        Transactions transaction = new Transactions(5L, 1L, 40.0, 0L, LocalDate.now());
        when(transactionsRepository.save(any(Transactions.class))).thenReturn(transaction);
        
        String result = transactionsService.addTransaction(transaction);
        
        assertEquals("Transaction is successfully added.", result);
        assertEquals(0L, transaction.getRewardPoints());
    }
    
    @Test
    void testGetByCustomerId() throws CustomerNotFoundException {
        when(transactionsRepository.existsById(1L)).thenReturn(true);
        when(transactionsRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(transaction1, transaction2));
        
        List<Transactions> transactions = transactionsService.getByCustomerId(1L);
        assertEquals(2, transactions.size());
    }
    
    @Test
    void testGetByCustomerId_NotFound() {
        when(transactionsRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(CustomerNotFoundException.class, () -> transactionsService.getByCustomerId(1L));
    }
    
    @Test
    void testGetRewardPoints() throws CustomerNotFoundException {
        when(transactionsRepository.existsById(1L)).thenReturn(true);
        when(transactionsRepository.findByCustomerIdAndDateBetween(1L, LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 31)))
                .thenReturn(Arrays.asList(transaction1, transaction2));
        
        Long points = transactionsService.getRewardPoints(1L, LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 31));
        
        assertEquals(120L, points);
    }
    
    @Test
    void testGetRewardPointsByMonth() throws CustomerNotFoundException {
        when(transactionsRepository.existsById(1L)).thenReturn(true);
        when(transactionsRepository.findByCustomerIdAndDateBetween(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(transaction1, transaction2));
        
        Map<String, Long> rewards = transactionsService.getRewardPointsByMonth(1L);
        assertEquals(3, rewards.size());
    }

    @Test
    void testGetAllCustomerRewards() {
        Transactions transaction3 = new Transactions(3L, 2L, 50.0, 0L, LocalDate.of(2025, 3, 5));
        when(transactionsRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2, transaction3));
        
        List<RewardDto> rewards = transactionsService.getAllCustomerRewards();
        assertEquals(2, rewards.size());
        assertEquals(120L, rewards.get(0).getRewardPoints());
        assertEquals(0L, rewards.get(1).getRewardPoints());
    }
}
