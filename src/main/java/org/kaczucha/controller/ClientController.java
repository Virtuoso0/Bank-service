package org.kaczucha.controller;

import lombok.RequiredArgsConstructor;
import org.kaczucha.controller.dto.ClientRequest;
import org.kaczucha.controller.dto.ClientResponse;
import org.kaczucha.service.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @GetMapping(path = "/api/users")
    public ResponseEntity<ClientResponse> findByEmail(@RequestParam String email) {
        final ClientResponse client = service.findResponseByEmail(email);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("EXAMPLE_HEADER", "DUMMY_VALUE");

        return new ResponseEntity<>(client, httpHeaders, HttpStatus.ACCEPTED);
    }

    //nie do końca poprawny sposób
    //powinniśmy stosować DTO
    @PostMapping(path = "/api/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createClient(@RequestBody ClientRequest client) {
        service.save(client);
    }
}
