package org.example.biblioapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas que no requieren iniciar sesión
                        .requestMatchers("/","/login", "/usuarios/new", "/usuarios/registro", "/css/**", "/js/**","/img/**").permitAll()
                        // Permitir que el formulario de guardar usuario funcione por POST sin loguearse
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Nuestra página personalizada de login
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true) // A dónde va tras iniciar sesión con éxito
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true) // Borra la sesión en el servidor
                        .clearAuthentication(true)   // Limpia la seguridad
                        .deleteCookies("JSESSIONID") // Elimina la cookie del navegador
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Encriptador de contraseñas obligatorio por seguridad
        return new BCryptPasswordEncoder();
    }
}
