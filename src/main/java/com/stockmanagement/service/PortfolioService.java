package com.stockmanagement.service;

import com.stockmanagement.dto.PortfolioResponseDto;
import com.stockmanagement.dto.PortfolioTransactionDto;
import com.stockmanagement.model.Portfolio;
import com.stockmanagement.model.Stock;
import com.stockmanagement.model.User;
import com.stockmanagement.repository.PortfolioRepository;
import com.stockmanagement.repository.StockRepository;
import com.stockmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PortfolioResponseDto buyStock(String username, PortfolioTransactionDto transactionDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Stock stock = stockRepository.findBySymbol(transactionDto.getStockSymbol())
                .orElseThrow(() -> new EntityNotFoundException("Stock not found"));

        Portfolio portfolio = portfolioRepository.findByUser(user)
                .orElseGet(() -> {
                    Portfolio newPortfolio = new Portfolio();
                    newPortfolio.setUser(user);
                    return newPortfolio;
                });

        // Add stock to portfolio
        portfolio.addStock(stock, transactionDto.getQuantity(), transactionDto.getPrice());
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        
        return modelMapper.map(savedPortfolio, PortfolioResponseDto.class);
    }

    @Transactional
    public PortfolioResponseDto sellStock(String username, PortfolioTransactionDto transactionDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Portfolio portfolio = portfolioRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));

        Stock stock = stockRepository.findBySymbol(transactionDto.getStockSymbol())
                .orElseThrow(() -> new EntityNotFoundException("Stock not found"));

        // Remove stock from portfolio
        portfolio.removeStock(stock, transactionDto.getQuantity(), transactionDto.getPrice());
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        
        return modelMapper.map(savedPortfolio, PortfolioResponseDto.class);
    }

    public PortfolioResponseDto getPortfolio(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Portfolio portfolio = portfolioRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));

        return modelMapper.map(portfolio, PortfolioResponseDto.class);
    }
}