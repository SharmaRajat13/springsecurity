package com.example.authenticator.repository;

import com.example.authenticator.entity.Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    public JpaRegisteredClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        if (registeredClient == null) {
            throw new IllegalArgumentException("RegisteredClient must not be null");
        }
        clientRepository.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be null or empty");
        }
        return clientRepository.findById(Long.valueOf(id))
                .map(this::toRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("ClientId must not be null or empty");
        }
        return clientRepository.findByClientId(clientId)
                .map(this::toRegisteredClient)
                .orElse(null);
    }

    private RegisteredClient toRegisteredClient(Client entity) {
        // Converts a JPA entity to RegisteredClient
        return RegisteredClient.withId(UUID.randomUUID().toString())
//        return RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantTypes(grants -> grants.addAll(Arrays.stream(entity.getGrantType().split(",")).map(AuthorizationGrantType::new).toList()))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .redirectUris(uris -> uris.addAll(entity.getRedirectUris()))
//                .scopes(scopes -> scopes.addAll(Arrays.stream(entity.getScope().split(",")).toList()))
                .scope("read")
                .build();
    }

    private Client toEntity(RegisteredClient registeredClient) {
        // Converts a RegisteredClient to JPA entity
        Client entity = new Client();
        entity.setId(Long.valueOf(registeredClient.getId()));
        entity.setClientId(registeredClient.getClientId());
        entity.setClientSecret(registeredClient.getClientSecret());
//        entity.setClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods());
        entity.setGrantType(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).collect(Collectors.joining(",")));
//        entity.setRedirectUris(registeredClient.getRedirectUris());
        entity.setScope(String.join(",", registeredClient.getScopes()));
        return entity;
    }


}
