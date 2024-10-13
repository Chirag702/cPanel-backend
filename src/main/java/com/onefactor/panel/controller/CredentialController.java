package com.onefactor.panel.controller;

import com.onefactor.panel.model.Credential;
import com.onefactor.panel.security.jwt.JwtUtils;
import com.onefactor.panel.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("/api/credentials")
public class CredentialController {
    @Autowired
    private JwtUtils jwtUtil;
    private final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public ResponseEntity<Credential> createCredential(@RequestBody Credential credential,@RequestHeader("Authorization") String token) {
    	String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userName= jwtUtil.getUserNameFromJwtToken(jwtToken);
        Credential createdCredential = credentialService.createCredential(credential,userName);
        return new ResponseEntity<>(createdCredential, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Credential>> getAllCredentials() {
        List<Credential> credentials = credentialService.getAllCredentials();
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Credential> getCredentialByName(@PathVariable String name, @RequestHeader("Authorization") String token) {
    	String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userName= jwtUtil.getUserNameFromJwtToken(jwtToken);
     
        Credential credential = credentialService.getCredentialByName(name,userName);
        return new ResponseEntity<>(credential, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Credential> updateCredential(@PathVariable Long id, @RequestBody Credential credential) {
        Credential updatedCredential = credentialService.updateCredential(id, credential);
        return new ResponseEntity<>(updatedCredential, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long id) {
        credentialService.deleteCredential(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
