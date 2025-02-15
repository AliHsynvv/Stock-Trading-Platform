package com.stockmanagement.controller;

import com.stockmanagement.dto.PortfolioResponseDto;
import com.stockmanagement.dto.PortfolioTransactionDto;
import com.stockmanagement.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<PortfolioResponseDto> getPortfolio(Authentication authentication) {
        return ResponseEntity.ok(portfolioService.getPortfolio(authentication.getName()));
    }

    @PostMapping("/buy")
    public ResponseEntity<PortfolioResponseDto> buyStock(
            Authentication authentication,
            @Valid @RequestBody PortfolioTransactionDto transactionDto) {
        return ResponseEntity.ok(portfolioService.buyStock(authentication.getName(), transactionDto));
    }

    @PostMapping("/sell")
    public ResponseEntity<PortfolioResponseDto> sellStock(
            Authentication authentication,
            @Valid @RequestBody PortfolioTransactionDto transactionDto) {
        return ResponseEntity.ok(portfolioService.sellStock(authentication.getName(), transactionDto));
    }
}