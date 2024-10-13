package com.onefactor.panel.repository;

import com.onefactor.panel.model.Credential;
import com.onefactor.panel.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

	Credential findByName(String name);
	// You can define custom query methods here if needed

	Credential findByNameAndUser(String name, Optional<User> user);
}
