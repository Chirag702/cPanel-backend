package com.onefactor.panel.service.impl;

import com.onefactor.panel.model.Credential;
import com.onefactor.panel.model.User;
import com.onefactor.panel.repository.CredentialRepository;
import com.onefactor.panel.repository.UserRepository;
import com.onefactor.panel.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CredentialServiceImpl implements CredentialService {

	private final CredentialRepository credentialRepository;
@Autowired
private UserRepository userRepo;
	
	@Autowired
	public CredentialServiceImpl(CredentialRepository credentialRepository) {
		this.credentialRepository = credentialRepository;
	}

	@Override
	public Credential createCredential(Credential credential, String username) {
		credential.setUser(userRepo.findByUsername(username).orElseThrow());
		return credentialRepository.save(credential);
	}

	@Override
	public List<Credential> getAllCredentials() {
		return credentialRepository.findAll();
	}

	@Override
	public Credential getCredentialById(Long id) {
		return credentialRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Credential not found with id: " + id));
	}

	@Override
	public Credential updateCredential(Long id, Credential credential) {
		Credential existingCredential = getCredentialById(id);
		existingCredential.setName(credential.getName());
		existingCredential.setUsername(credential.getUsername());
		existingCredential.setPassword(credential.getPassword());
		return credentialRepository.save(existingCredential);
	}

	@Override
	public void deleteCredential(Long id) {
		credentialRepository.deleteById(id);
	}

	@Override
	public Credential getCredentialByName(String name, String username) {
		Optional<User> user=userRepo.findByUsername(username);
		return credentialRepository.findByNameAndUser(name,user);
	}
}
