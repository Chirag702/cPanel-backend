package com.onefactor.panel.controller;

import com.onefactor.panel.model.BusinessProcess;
import com.onefactor.panel.security.jwt.JwtUtils;
import com.onefactor.panel.service.BusinessProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("/api/business-process")
public class BusinessProcessController {

	@Autowired
	private BusinessProcessService businessProcessService;

	@Autowired
	private JwtUtils jwtUtil;

	// POST Mapping to create a new Business Process
	@PostMapping
	public ResponseEntity<BusinessProcess> createBusinessProcess(@RequestBody BusinessProcess businessProcess,
			@RequestHeader("Authorization") String token) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String userName = jwtUtil.getUserNameFromJwtToken(jwtToken); // Assuming you have a JwtUtil class

		// Call service to save business process
		BusinessProcess createdProcess = businessProcessService.save(businessProcess);
		return new ResponseEntity<>(createdProcess, HttpStatus.CREATED);
	}

	// GET Mapping to get all Business Processes
	@GetMapping
	public ResponseEntity<Iterable<BusinessProcess>> getAllBusinessProcesses() {
		Iterable<BusinessProcess> processes = businessProcessService.findAll();
		return new ResponseEntity<>(processes, HttpStatus.OK);
	}

	// GET Mapping to get a Business Process by ID
	@GetMapping("/{id}")
	public ResponseEntity<BusinessProcess> getBusinessProcessById(@PathVariable Long id) {
		BusinessProcess process = businessProcessService.findById(id);
		return new ResponseEntity<>(process, HttpStatus.OK);
	}

	// DELETE Mapping to delete a Business Process by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBusinessProcess(@PathVariable Long id) {
		businessProcessService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// PUT Mapping to update an existing Business Process
	@PutMapping("/{id}")
	public ResponseEntity<BusinessProcess> updateBusinessProcess(@PathVariable Long id,
			@RequestBody BusinessProcess businessProcess, @RequestHeader("Authorization") String token) {
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		String userName = jwtUtil.getUserNameFromJwtToken(jwtToken); // Assuming you have a JwtUtil class

		// Find the existing process
		BusinessProcess existingProcess = businessProcessService.findById(id);
		if (existingProcess == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Update fields of the existing process
		existingProcess.setName(businessProcess.getName()); // Example of updating a name, adjust based on your fields
		existingProcess.setCommand(businessProcess.getCommand());
		existingProcess.setComment(businessProcess.getComment());
	
		// Save the updated process
		BusinessProcess updatedProcess = businessProcessService.save(existingProcess);
		return new ResponseEntity<>(updatedProcess, HttpStatus.OK);
	}
}
