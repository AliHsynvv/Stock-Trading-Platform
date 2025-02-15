package com.stockmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class PortfolioTransactionDto {
    @NotNull(message = "Stock symbol is required")
    private String stockSymbol;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @NotNull(message = "Transaction price is required")
    @Positive(message = "Transaction price must be positive")
    private BigDecimal price;
}