package org.kaczucha.controller;

import lombok.RequiredArgsConstructor;
import org.kaczucha.repository.entity.Client;
import org.kaczucha.service.BankService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BankController {
    private final BankService service;

    @GetMapping(path = "/api/users")
    public ResponseEntity<Client> findByEmail(@RequestParam String email) {
        final Client client = service.findByEmail(email);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("headerName", "headerValue");

        return new ResponseEntity<Client>(client, httpHeaders, HttpStatus.ACCEPTED);
    }

    //nie do końca poprawny sposób
    //powinniśmy stosować DTO
    @PostMapping(path = "/api/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createClient(@RequestBody Client client) {
        service.save(client);
    }
}
