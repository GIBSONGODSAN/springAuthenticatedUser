package com.dtcc.intern.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dtcc.intern.demo.model.TradingAccount;

@Repository
public interface TradingAccountRepository extends JpaRepository<TradingAccount, Integer> {
}
