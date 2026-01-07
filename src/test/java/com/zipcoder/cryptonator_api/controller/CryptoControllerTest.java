package com.zipcoder.cryptonator_api.controller;

import com.zipcoder.cryptonator_api.services.CryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CryptoController.class)
@ActiveProfiles("test")
class CryptoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoService cryptoService;

    @Test
    void testAddCrypto_Failure() throws Exception {
        when(cryptoService.fetchAndSaveCrypto("btc")).thenReturn(null);

        mockMvc.perform(post("/api/crypto/btc"))
                .andExpect(status().isBadRequest());
    }
}