package com.zipcoder.cryptonator_api.services;


import com.zipcoder.cryptonator_api.model.CryptoCurrency;
import com.zipcoder.cryptonator_api.repositories.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Map;


@Service
public class CryptoService {
    
    @Autowired
    private CryptoRepository cryptoRepository;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // CoinGecko API - Free, no API key needed
    private static final String COINGECKO_API = "https://api.coingecko.com/api/v3/simple/price?ids=";
    
    // Get all cryptocurrencies from database
    public List<CryptoCurrency> getAllCryptos() {
        return cryptoRepository.findAll();
    }
    
    // Get cryptocurrency by ticker
    public Optional<CryptoCurrency> getCryptoByTicker(String ticker) {
        return cryptoRepository.findByTicker(ticker.toUpperCase());
    }
    
    // Fetch and save cryptocurrency data from CoinGecko
    public CryptoCurrency fetchAndSaveCrypto(String coinId) {
        try {
            // CoinGecko uses coin IDs like "bitcoin", "ethereum"
            String url = COINGECKO_API + coinId.toLowerCase() + "&vs_currencies=usd&include_24hr_change=true";
            
            // Call CoinGecko API
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey(coinId.toLowerCase())) {
                Map<String, Object> coinData = (Map<String, Object>) response.get(coinId.toLowerCase());
                
                Double price = ((Number) coinData.get("usd")).doubleValue();
                Double changePercent = coinData.get("usd_24h_change") != null ? 
                    ((Number) coinData.get("usd_24h_change")).doubleValue() : 0.0;
                
                String change = String.format("%.2f%%", changePercent);
                String ticker = coinId.toUpperCase();
                
                CryptoCurrency crypto;
                Optional<CryptoCurrency> existing = cryptoRepository.findByTicker(ticker);
                
                if (existing.isPresent()) {
                    // Update existing
                    crypto = existing.get();
                    crypto.setPrice(price);
                    crypto.setChange(change);
                    crypto.setLastUpdated(LocalDateTime.now());
                } else {
                    // Create new
                    crypto = new CryptoCurrency(
                        ticker,
                        price,
                        change,
                        LocalDateTime.now()
                    );
                }
                
                return cryptoRepository.save(crypto);
            }
            
        } catch (Exception e) {
            System.err.println("Error fetching crypto data from CoinGecko: " + e.getMessage());
        }
        
        return null;
    }
    
    // Scheduled task: Update all tracked cryptos every 5 minutes
    @Scheduled(fixedRate = 300000) // 300,000 ms = 5 minutes
    public void updateAllCryptos() {
        System.out.println("Updating all cryptocurrencies at " + LocalDateTime.now());
        
        List<CryptoCurrency> cryptos = cryptoRepository.findAll();
        
        for (CryptoCurrency crypto : cryptos) {
            fetchAndSaveCrypto(crypto.getTicker().toLowerCase());
        }
        
        System.out.println("Update complete!");
    }
}


