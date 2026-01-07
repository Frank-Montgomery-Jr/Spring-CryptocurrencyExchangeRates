package com.zipcoder.cryptonator_api.model;

  
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "cryptocurrencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String ticker;  // BTC, ETH, etc.
    
    @Column(nullable = false)
    private Double price;
    
    private String change;  // +2.5%, -1.2%, etc.
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    // Constructor without id (for creating new entries)
    public CryptoCurrency(String ticker, Double price, String change, LocalDateTime lastUpdated) {
        this.ticker = ticker;
        this.price = price;
        this.change = change;
        this.lastUpdated = lastUpdated;
    }
}

