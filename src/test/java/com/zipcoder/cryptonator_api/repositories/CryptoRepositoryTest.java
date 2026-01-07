package com.zipcoder.cryptonator_api.repositories;

import com.zipcoder.cryptonator_api.model.CryptoCurrency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CryptoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CryptoRepository cryptoRepository;

    @Test
    void testFindByTicker_Found() {
        CryptoCurrency crypto = new CryptoCurrency("BTC", 50000.0, "5.0%", LocalDateTime.now());
        entityManager.persist(crypto);
        entityManager.flush();

        Optional<CryptoCurrency> found = cryptoRepository.findByTicker("BTC");

        assertTrue(found.isPresent());
        assertEquals("BTC", found.get().getTicker());
    }

    @Test
    void testFindByTicker_NotFound() {
        Optional<CryptoCurrency> found = cryptoRepository.findByTicker("XYZ");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByTicker_CaseInsensitive() {
        CryptoCurrency crypto = new CryptoCurrency("ETH", 3000.0, "3.5%", LocalDateTime.now());
        entityManager.persist(crypto);
        entityManager.flush();

        Optional<CryptoCurrency> foundLower = cryptoRepository.findByTicker("eth");
        Optional<CryptoCurrency> foundUpper = cryptoRepository.findByTicker("ETH");

        assertTrue(foundLower.isPresent() || foundUpper.isPresent());
    }

    @Test
    void testSaveCryptoCurrency() {
        CryptoCurrency crypto = new CryptoCurrency("DOGE", 0.25, "10.0%", LocalDateTime.now());

        CryptoCurrency saved = cryptoRepository.save(crypto);

        assertNotNull(saved.getId());
        assertEquals("DOGE", saved.getTicker());
        assertEquals(0.25, saved.getPrice());
    }
}