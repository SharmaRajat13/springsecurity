package com.example.authenticator.util;

import com.example.authenticator.entity.Client;
import com.example.authenticator.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ClientInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientInitializer(ClientRepository clientRepository, PasswordEncoder passwordEncoder){

        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (clientRepository.findByClientId("demo").isEmpty()) {
            Client client = new Client();
            client.setClientId("demo");
            client.setClientSecret(passwordEncoder.encode("mysecret")); // Hash the secret
            client.setScope("read write");
            client.setGrantType("client_credentials");
            clientRepository.save(client);
        }
    }
}
