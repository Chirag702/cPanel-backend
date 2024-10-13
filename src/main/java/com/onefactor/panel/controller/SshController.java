package com.onefactor.panel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onefactor.panel.model.SSHRequest;
import com.onefactor.panel.security.jwt.JwtUtils;
import com.onefactor.panel.service.SshService;

import io.jsonwebtoken.MalformedJwtException;

@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("/ssh")
public class SshController {
	private static final Logger logger = LoggerFactory.getLogger(SshController.class);

	@Autowired
	private SshService sshService;

	@Autowired
	private JwtUtils jwtUtil;

	@MessageMapping("/executeCommand")
	@SendTo("/topic/commandOutput")
	public String executeCommand(@Payload SSHRequest sshRequest, 
	                              @Header("Authorization") String token) {
	    StringBuilder finalOutput = new StringBuilder();

	    try {
	        logger.info("Received token: {}", token); // Log the token to debug

	        // Strip "Bearer " prefix if present
	        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

	        if (jwtToken.isEmpty()) {
	            logger.error("Received an empty JWT token");
	            return "Invalid token: Token is empty.";
	        }

	        // Extract username from JWT token
	        String userName = jwtUtil.getUserNameFromJwtToken(jwtToken);
	        logger.info("Received SSH request for user: {}, commands: {}", userName, sshRequest.getCommands());

	        // Execute commands
	        for (String command : sshRequest.getCommands()) {
	            logger.info("Executing command: {}", command);
	            String output = sshService.executeCommand(command, userName);

	            if (output == null || output.isEmpty()) {
	                logger.warn("No output received for command: {}", command);
	                finalOutput.append("No output received for command '").append(command).append("'\n");
	            } else {
	                finalOutput.append("Output of command '").append(command).append("':\n").append(output).append("\n");
	                logger.info("Output of command '{}':\n{}", command, output);
	            }
	        }
	    } catch (MalformedJwtException e) {
	        logger.error("Malformed JWT: {}", e.getMessage());
	        return "Invalid token: " + e.getMessage();
	    } catch (Exception e) {
	        logger.error("Error processing SSH command request: ", e);
	        return "Error executing SSH command: " + e.getMessage();
	    }

	    return finalOutput.toString();
	}
}