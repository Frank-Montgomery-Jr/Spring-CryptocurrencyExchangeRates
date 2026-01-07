
package com.zipcoder.cryptonator_api.repositories;


import com.zipcoder.cryptonator_api.model.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface CryptoRepository extends JpaRepository<CryptoCurrency, Long> {

    //find cryptoCurrency by ticker (e.g. , "btc", eth)
    Optional<CryptoCurrency> findByTicker(String ticker);

    //check if ticker exists
    boolean existsByTicker(String ticker);

    //delete by ticker
    void deleteByTicker(String ticker);
}

