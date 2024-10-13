package com.onefactor.panel.service;

import com.onefactor.panel.model.BusinessProcess;

import java.util.List;

public interface BusinessProcessService {
    BusinessProcess save(BusinessProcess businessProcess);
    Iterable<BusinessProcess> findAll();
    BusinessProcess findById(Long id);
    void delete(Long id);
}
