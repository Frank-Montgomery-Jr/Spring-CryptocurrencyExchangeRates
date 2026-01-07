package com.zipcoder.cryptonator_api.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CryptoCurrencyTest {

    @Test
    void testCryptoCurrencyConstructor() {
        String ticker = "BTC";
        Double price = 50000.0;
        String change = "5.0%";
        LocalDateTime now = LocalDateTime.now();

        CryptoCurrency crypto = new CryptoCurrency(ticker, price, change, now);

        assertEquals(ticker, crypto.getTicker());
        assertEquals(price, crypto.getPrice());
        assertEquals(change, crypto.getChange());
        assertEquals(now, crypto.getLastUpdated());
    }

    @Test
    void testSettersAndGetters() {
        CryptoCurrency crypto = new CryptoCurrency();

        crypto.setTicker("ETH");
        crypto.setPrice(3000.0);
        crypto.setChange("3.5%");
        LocalDateTime time = LocalDateTime.now();
        crypto.setLastUpdated(time);

        assertEquals("ETH", crypto.getTicker());
        assertEquals(3000.0, crypto.getPrice());
        assertEquals("3.5%", crypto.getChange());
        assertEquals(time, crypto.getLastUpdated());
    }

    @Test
    void testIdGeneration() {
        CryptoCurrency crypto = new CryptoCurrency("BTC", 50000.0, "5.0%", LocalDateTime.now());

        assertNull(crypto.getId());
    }
}

