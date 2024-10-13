package com.onefactor.panel.service.impl;

import com.onefactor.panel.model.BusinessProcess;
import com.onefactor.panel.repository.BusinessProcessRepository; // Assuming you have this repository
import com.onefactor.panel.service.BusinessProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessProcessServiceImpl implements BusinessProcessService {

    @Autowired
    private BusinessProcessRepository businessProcessRepository;

    @Override
    public BusinessProcess save(BusinessProcess businessProcess) {
        // Here you can add any logic related to the business process before saving
        return businessProcessRepository.save(businessProcess);
    }

    @Override
    public Iterable<BusinessProcess> findAll() {
        return businessProcessRepository.findAll();
    }

    @Override
    public BusinessProcess findById(Long id) {
        return businessProcessRepository.findById(id).orElse(null); // Handle the case when not found
    }

    @Override
    public void delete(Long id) {
        businessProcessRepository.deleteById(id);
    }
}
