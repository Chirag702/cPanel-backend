package com.onefactor.panel.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onefactor.panel.model.Apps;
import com.onefactor.panel.service.AppsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(allowedHeaders = "*")

@RestController
@RequestMapping("/api/apps")
public class AppsController {

	@Autowired
	private AppsService appsService;

	// Create a new App
	@PostMapping
	public ResponseEntity<Apps> createApp(@RequestBody Apps app) {
		Apps savedApp = appsService.createApp(app);
		return new ResponseEntity<>(savedApp, HttpStatus.CREATED);
	}

	// Get all Apps
	@GetMapping
	public ResponseEntity<List<Apps>> getAllApps() {
		List<Apps> apps = appsService.getAllApps();
		return new ResponseEntity<>(apps, HttpStatus.OK);
	}

	// Get an App by ID
	@GetMapping("/{id}")
	public ResponseEntity<Apps> getAppById(@PathVariable Long id) {
		Optional<Apps> app = appsService.getAppById(id);
		return app.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Update an App
	@PutMapping("/{id}")
	public ResponseEntity<Apps> updateApp(@PathVariable Long id, @RequestBody Apps appDetails) {
		Apps updatedApp = appsService.updateApp(id, appDetails);
		return ResponseEntity.ok(updatedApp);
	}

	// Delete an App
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteApp(@PathVariable Long id) {
		appsService.deleteApp(id);
		return ResponseEntity.noContent().build();
	}
}
