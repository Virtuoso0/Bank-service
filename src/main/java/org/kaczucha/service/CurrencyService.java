package org.kaczucha.service;

import lombok.AllArgsConstructor;
import org.kaczucha.service.dto.CurrencyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CurrencyService {

    private final RestTemplate restTemplate;

    public double getCurrencyRates(String currencyCode) {
        final ResponseEntity<CurrencyResponse> response = restTemplate.getForEntity("https://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "?format=json", CurrencyResponse.class);
        if(response.getStatusCode().isError())
            throw new IllegalArgumentException("Invalid URL");

        CurrencyResponse body = response.getBody();
        assert body != null;
        return Double.parseDouble(body.getRates()[0].get("mid"));
    }
}
