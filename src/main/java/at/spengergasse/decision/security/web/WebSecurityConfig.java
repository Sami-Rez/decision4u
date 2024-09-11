package at.spengergasse.decision.security.web;

import at.spengergasse.decision.domain.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // AUTHENTICATION
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(AbstractHttpConfigurer::disable);

        // AUTHORIZATION
        http.authorizeHttpRequests(
                (authorize) ->
                        authorize
                                .requestMatchers("/api/registration/**")
                                .permitAll()
                                .requestMatchers("/api/user/**")
                                .hasRole(Role.USER.toString())
                                .anyRequest()
                                .authenticated());

        // CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS
        http.cors(AbstractHttpConfigurer::disable);

        // SESSIONS
        http.sessionManagement(
                session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
