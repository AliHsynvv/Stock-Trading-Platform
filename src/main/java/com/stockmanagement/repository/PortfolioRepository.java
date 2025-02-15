package com.stockmanagement.repository;

import com.stockmanagement.model.Portfolio;
import com.stockmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findAllByUser(User user);
    
    @Query("SELECT p FROM Portfolio p JOIN p.holdings h WHERE p.user = :user AND KEY(h).symbol = :symbol")
    List<Portfolio> findByUserAndStockSymbol(@Param("user") User user, @Param("symbol") String symbol);
    
    Optional<Portfolio> findByUser(User user);
}