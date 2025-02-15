package com.stockmanagement.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PortfolioResponseDto {
    private Long id;
    private String username;
    private List<PortfolioHoldingDto> holdings;
    private BigDecimal totalValue;
}

@Data
class PortfolioHoldingDto {
    private String stockSymbol;
    private String companyName;
    private Integer quantity;
    private BigDecimal currentPrice;
    private BigDecimal totalValue;
}