package com.example.authenticator.config;

import com.example.authenticator.entity.Client;
import com.example.authenticator.repository.ClientRepository;
import com.example.authenticator.repository.JpaRegisteredClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
        http.with(new OAuth2AuthorizationServerConfigurer(),Customizer.withDefaults());

        return http
//        http
               .csrf(csrf -> csrf
                       .ignoringRequestMatchers("/oauth2/token") // Disable CSRF for the token endpoint
               )
                .build();
//       return http
//               .csrf(csrf -> csrf
//                       .ignoringRequestMatchers("/oauth2/token") // Disable CSRF for the token endpoint
//               )
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/oauth2/token").permitAll()
//                                .anyRequest().authenticated()
//                )
////                        auth.anyRequest().authenticated())
//                .oauth2ResourceServer(
//                        httpSecurityOAuth2ResourceServerConfigurer ->  httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults())
//                )
////               .with(OAuth2AuthorizationServerConfigurer,Customizer.withDefaults())
//
//               .build();
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(ClientRepository clientRepository) {
        return new JpaRegisteredClientRepository(clientRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8080")
                .build();
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(30)) // Short-lived access tokens
//                .refreshTokenTimeToLive(Duration.ofDays(30))   // Long-lived refresh tokens
                .reuseRefreshTokens(false)                    // Enforce one-time use of refresh tokens
                .build();
    }
//    @Bean
//    public void saveSampleClient(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
//        if (clientRepository.findByClientId("demo").isEmpty()) {
//            Client client = new Client();
//            client.setClientId("demo");
//            client.setClientSecret(passwordEncoder.encode("mysecret")); // Hash the secret
//            client.setScope("read write");
//            client.setGrantType("client_credentials");
//            clientRepository.save(client);
//        }
//    }


}

