package org.kaczucha.controller;

import lombok.AllArgsConstructor;
import org.kaczucha.controller.dto.AccountRequest;
import org.kaczucha.controller.dto.AccountResponse;
import org.kaczucha.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountService service;

    @GetMapping(path = "/api/account")
    public ResponseEntity<AccountResponse> findById(@RequestParam Long id) {
        final AccountResponse account = service.findById(id);
        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    //nie do końca poprawny sposób
    //powinniśmy stosować DTO
    @PostMapping(path = "/api/account")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        service.save(accountRequest);
    }
}
