package com.stockmanagement.service;

import com.stockmanagement.dto.StockDto;
import com.stockmanagement.model.Stock;
import com.stockmanagement.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;

    public List<StockDto> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(stock -> modelMapper.map(stock, StockDto.class))
                .collect(Collectors.toList());
    }

    public StockDto getStockBySymbol(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with symbol: " + symbol));
        return modelMapper.map(stock, StockDto.class);
    }

    public StockDto createStock(StockDto stockDto) {
        Stock stock = modelMapper.map(stockDto, Stock.class);
        Stock savedStock = stockRepository.save(stock);
        return modelMapper.map(savedStock, StockDto.class);
    }

    public StockDto updateStock(String symbol, StockDto stockDto) {
        Stock existingStock = stockRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with symbol: " + symbol));
        
        modelMapper.map(stockDto, existingStock);
        Stock updatedStock = stockRepository.save(existingStock);
        return modelMapper.map(updatedStock, StockDto.class);
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            updateStockPrice(stock);
        }
    }

    private void updateStockPrice(Stock stock) {
        try {
            simulatePriceChange(stock);
            stock.setLastUpdated(LocalDateTime.now());
            stockRepository.save(stock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simulatePriceChange(Stock stock) {
        BigDecimal currentPrice = stock.getCurrentPrice();
        Random random = new Random();
        // Simulate up to 2% price change
        double changePercent = (random.nextDouble() * 4 - 2) / 100;
        BigDecimal newPrice = currentPrice.multiply(BigDecimal.valueOf(1 + changePercent));
        stock.setCurrentPrice(newPrice);
    }
}