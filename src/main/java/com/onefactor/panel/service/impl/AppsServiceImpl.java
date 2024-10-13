package com.onefactor.panel.service.impl;

 
import com.onefactor.panel.model.Apps;
import com.onefactor.panel.repository.AppsRepository;
import com.onefactor.panel.service.AppsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppsServiceImpl implements AppsService {

    @Autowired
    private AppsRepository appsRepository;

    @Override
    public Apps createApp(Apps app) {
        return appsRepository.save(app);
    }

    @Override
    public List<Apps> getAllApps() {
        return appsRepository.findAll();
    }

    @Override
    public Optional<Apps> getAppById(Long id) {
        return appsRepository.findById(id);
    }

    @Override
    public Apps updateApp(Long id, Apps appDetails) {
        Apps app = appsRepository.findById(id).orElseThrow(() -> new RuntimeException("App not found"));
       app.setName(appDetails.getName());
       app.setDescription(appDetails.getDescription());
       app.setGitUrl(appDetails.getGitUrl());
       app.setBuildCommand(appDetails.getBuildCommand());
       app.setStart(appDetails.getStart());
       app.setStop(appDetails.getStop());
       
         return appsRepository.save(app);
    }

    @Override
    public void deleteApp(Long id) {
        appsRepository.deleteById(id);
    }
}
