package com.onefactor.panel.service;

 
import com.onefactor.panel.model.Apps;

import java.util.List;
import java.util.Optional;

public interface AppsService {
    Apps createApp(Apps app);
    List<Apps> getAllApps();
    Optional<Apps> getAppById(Long id);
    Apps updateApp(Long id, Apps appDetails);
    void deleteApp(Long id);
}
