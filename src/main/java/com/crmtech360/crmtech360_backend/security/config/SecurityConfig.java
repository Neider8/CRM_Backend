package com.crmtech360.crmtech360_backend.security.config;

import com.crmtech360.crmtech360_backend.security.jwt.JwtAuthenticationEntryPoint;
import com.crmtech360.crmtech360_backend.security.jwt.JwtRequestFilter;
import com.crmtech360.crmtech360_backend.service.impl.UserDetailsServiceImpl; // Asegúrate que el nombre del bean sea userDetailsService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Para CSRF y Headers
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // Habilita @PreAuthorize, @Secured, @RolesAllowed
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsServiceImpl userDetailsServiceImpl; // Inyectar para el AuthenticationManagerBuilder

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtRequestFilter jwtRequestFilter,
                          UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Opcional: Si necesitas configurar AuthenticationManagerBuilder directamente (menos común con config actual)
    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }
    */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Aplicar configuración CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Deshabilitar CSRF ya que usamos JWT (API stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // Configurar el punto de entrada para excepciones de autenticación
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                // Configurar la gestión de sesiones como STATELESS (sin sesiones en el servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configurar las reglas de autorización para las solicitudes HTTP
                .authorizeHttpRequests(auth -> auth
                        // --- ENDPOINTS PÚBLICOS ---
                        .requestMatchers("/api/v1/auth/**").permitAll() // Login y Registro
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/favicon.ico" // A menudo solicitado por navegadores
                        ).permitAll()

                        // --- ENDPOINTS PROTEGIDOS ---
                        // La autorización específica se manejará con @PreAuthorize en los controladores.
                        // Aquí definimos que cualquier otra solicitud debe estar autenticada.
                        .anyRequest().authenticated()
                );

        // Añadir el filtro JWT antes del filtro estándar de autenticación por usuario/contraseña
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite todos los orígenes. En producción, deberías restringirlo a los dominios de tu frontend.
        // Ejemplo: configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://tufrontend.com"));
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization")); // Si necesitas exponer algún header custom
        configuration.setAllowCredentials(true); // Permitir credenciales si es necesario (cookies, autenticación básica)
        configuration.setMaxAge(3600L); // Tiempo en segundos que el resultado de una petición pre-flight puede ser cacheado

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica esta configuración a todas las rutas
        return source;
    }
}