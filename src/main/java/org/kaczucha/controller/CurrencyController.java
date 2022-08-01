package org.kaczucha.controller;

import lombok.RequiredArgsConstructor;
import org.kaczucha.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService service;

    @GetMapping(path = "/api/currency")
    public ResponseEntity<Double> get(@RequestParam String code) {
        final double price = service.getCurrencyRates(code);

        return new ResponseEntity<>(price, HttpStatus.ACCEPTED);
    }
}
