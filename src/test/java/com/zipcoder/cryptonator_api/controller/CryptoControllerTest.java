package com.zipcoder.cryptonator_api.controller;

import com.zipcoder.cryptonator_api.model.CryptoCurrency;
import com.zipcoder.cryptonator_api.services.CryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CryptoController.class)
public class CryptoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoService cryptoService;

    @Test
    public void testGetAllCryptos() throws Exception {
        // Arrange
        CryptoCurrency bitcoin = new CryptoCurrency(1L, "BITCOIN", 45000.0, "+2.5%", LocalDateTime.now());
        CryptoCurrency ethereum = new CryptoCurrency(2L, "ETHEREUM", 3000.0, "-1.2%", LocalDateTime.now());
        List<CryptoCurrency> cryptos = Arrays.asList(bitcoin, ethereum);

        when(cryptoService.getAllCryptos()).thenReturn(cryptos);

        // Act & Assert
        mockMvc.perform(get("/api/crypto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticker").value("BITCOIN"))
                .andExpect(jsonPath("$[0].price").value(45000.0))
                .andExpect(jsonPath("$[1].ticker").value("ETHEREUM"))
                .andExpect(jsonPath("$[1].price").value(3000.0));
    }

    @Test
    public void testGetCryptoByTicker_Found() throws Exception {
        // Arrange
        CryptoCurrency bitcoin = new CryptoCurrency(1L, "BITCOIN", 45000.0, "+2.5%", LocalDateTime.now());
        when(cryptoService.getCryptoByTicker("BITCOIN")).thenReturn(Optional.of(bitcoin));

        // Act & Assert
        mockMvc.perform(get("/api/crypto/BITCOIN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("BITCOIN"))
                .andExpect(jsonPath("$.price").value(45000.0))
                .andExpect(jsonPath("$.change").value("+2.5%"));
    }

    @Test
    public void testGetCryptoByTicker_NotFound() throws Exception {
        // Arrange
        when(cryptoService.getCryptoByTicker("UNKNOWN")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/crypto/UNKNOWN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCrypto_Success() throws Exception {
        // Arrange
        CryptoCurrency bitcoin = new CryptoCurrency(1L, "BITCOIN", 45000.0, "+2.5%", LocalDateTime.now());
        when(cryptoService.fetchAndSaveCrypto("bitcoin")).thenReturn(bitcoin);

        // Act & Assert
        mockMvc.perform(post("/api/crypto/bitcoin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticker").value("BITCOIN"))
                .andExpect(jsonPath("$.price").value(45000.0));
    }

    @Test
    public void testAddCrypto_Failure() throws Exception {
        // Arrange
        when(cryptoService.fetchAndSaveCrypto("invalid")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/crypto/invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
