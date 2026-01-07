package com.zipcoder.cryptonator_api.controller;

import com.zipcoder.cryptonator_api.model.CryptoCurrency;
import com.zipcoder.cryptonator_api.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    // get all cryptocurrencies
    // http://localhost:8080/api/crypto
    @GetMapping
    public ResponseEntity<List<CryptoCurrency>> getAllCryptos() {
        List<CryptoCurrency> cryptos = cryptoService.getAllCryptos();
        return ResponseEntity.ok(cryptos);
    }


    //get cryptocurrency by ticker
    //http://localhost:8080/api/crypto/bitcoin
    @GetMapping("/{ticker}")
    public ResponseEntity<CryptoCurrency> getCryptoByTicker(@PathVariable String ticker) {
       Optional<CryptoCurrency> crypto = cryptoService.getCryptoByTicker(ticker);
       
       if (crypto.isPresent()) {
        return ResponseEntity.ok(crypto.get());
       } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
    }

    //POST - Add/Update crypto (fetch from coingecko)
    //http://localhost:8080/api/crypto/bitcoin
    @PostMapping("/{coinId}")
    public ResponseEntity<CryptoCurrency> addCrypto(@PathVariable String coinId) {
        CryptoCurrency crypto = cryptoService.fetchAndSaveCrypto(coinId);
        
        if (crypto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(crypto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }   
    }
}
    


