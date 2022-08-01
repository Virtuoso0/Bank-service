package org.kaczucha.service.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CurrencyResponse {

    private String code;
    private Map<String, String>[] rates;

}
