package com.onefactor.panel.service;

import com.onefactor.panel.model.Credential;
import java.util.List;

public interface CredentialService {
    Credential createCredential(Credential credential, String userName);
    List<Credential> getAllCredentials();
    Credential getCredentialById(Long id);
    Credential updateCredential(Long id, Credential credential);
    void deleteCredential(Long id);
	Credential getCredentialByName(String name, String userName);
}
