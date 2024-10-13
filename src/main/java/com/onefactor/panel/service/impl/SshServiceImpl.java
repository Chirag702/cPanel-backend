package com.onefactor.panel.service.impl;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.onefactor.panel.model.BusinessProcess;
import com.onefactor.panel.model.Credential;
import com.onefactor.panel.model.User;
import com.onefactor.panel.repository.BusinessProcessRepository;
import com.onefactor.panel.repository.CredentialRepository;
import com.onefactor.panel.repository.UserRepository;
import com.onefactor.panel.service.SshService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Properties;

@Service
public class SshServiceImpl implements SshService {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private BusinessProcessRepository bpRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CredentialRepository credRepo;

	@Override
	public String executeCommand(String command, String username) {
		Optional<User> user = userRepo.findByUsername(username);
		Credential credential= credRepo.findByNameAndUser("ssh", user);
		StringBuilder output = new StringBuilder();
		Session session = null;
		ChannelExec channel = null;


		try {
			JSch jsch = new JSch();
			session = jsch.getSession(credential.getUsername(), credential.getHost(), 22);
			session.setPassword(credential.getPassword());

			// Avoid asking for key confirmation
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			// Connect to the SSH server
			session.connect();

			// Execute the command
			channel = (ChannelExec) session.openChannel("exec");
			BusinessProcess getBP = bpRepo.findByName(command);

			if (getBP == null) {
				return "Error: BusinessProcess not found for command: " + command;
			}

			channel.setCommand(getBP.getCommand());
			channel.setInputStream(null); // Prevents any input requirement on the channel
			channel.setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect(10000); // 10-second connection timeout

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;

			// Read output in real time
			while (true) {
				while ((line = reader.readLine()) != null) {
					output.append(line).append("\n");
					messagingTemplate.convertAndSend("/topic/commandOutput", line); // Send line to WebSocket
				}
				// Check if channel is closed
				if (channel.isClosed() && reader.ready() == false) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}

		return output.toString();
	}

}
