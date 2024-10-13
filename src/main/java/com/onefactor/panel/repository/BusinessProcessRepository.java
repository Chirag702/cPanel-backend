package com.onefactor.panel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onefactor.panel.model.BusinessProcess;

@Repository
public interface BusinessProcessRepository extends CrudRepository<BusinessProcess, Long> {

	BusinessProcess findByName(String command);

}
