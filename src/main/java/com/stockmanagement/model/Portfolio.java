package com.stockmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "portfolio_holdings")
    @MapKeyJoinColumn(name = "stock_id")
    @Column(name = "quantity")
    private Map<Stock, Integer> holdings = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "portfolio_average_prices")
    @MapKeyJoinColumn(name = "stock_id")
    @Column(name = "average_price")
    private Map<Stock, BigDecimal> averagePurchasePrices = new HashMap<>();

    public void addStock(Stock stock, Integer quantity, BigDecimal price) {
        Integer currentQuantity = holdings.getOrDefault(stock, 0);
        BigDecimal currentAvgPrice = averagePurchasePrices.getOrDefault(stock, BigDecimal.ZERO);
        
        // Calculate new average price
        if (currentQuantity > 0) {
            BigDecimal totalValue = currentAvgPrice.multiply(new BigDecimal(currentQuantity))
                    .add(price.multiply(new BigDecimal(quantity)));
            BigDecimal newQuantity = new BigDecimal(currentQuantity + quantity);
            averagePurchasePrices.put(stock, totalValue.divide(newQuantity, 2, RoundingMode.HALF_UP));
        } else {
            averagePurchasePrices.put(stock, price);
        }
        
        // Update quantity
        holdings.put(stock, currentQuantity + quantity);
    }

    public void removeStock(Stock stock, Integer quantity, BigDecimal price) {
        Integer currentQuantity = holdings.getOrDefault(stock, 0);
        if (currentQuantity < quantity) {
            throw new IllegalArgumentException("Insufficient stock quantity");
        }
        
        int remainingQuantity = currentQuantity - quantity;
        if (remainingQuantity == 0) {
            holdings.remove(stock);
            averagePurchasePrices.remove(stock);
        } else {
            holdings.put(stock, remainingQuantity);
        }
    }

    public BigDecimal getCurrentValue() {
        return holdings.entrySet().stream()
                .map(entry -> entry.getKey().getCurrentPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getProfitLoss() {
        return holdings.entrySet().stream()
                .map(entry -> entry.getKey().getCurrentPrice().multiply(BigDecimal.valueOf(entry.getValue()))
                        .subtract(averagePurchasePrices.get(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}