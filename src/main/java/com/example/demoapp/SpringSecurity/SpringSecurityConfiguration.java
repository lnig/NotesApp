package com.example.demoapp.SpringSecurity;
import com.example.demoapp.Services.AuthorityService;
import com.example.demoapp.Services.CategoriesService;
import com.example.demoapp.Services.NotesService;
import com.example.demoapp.repositories.CategoriesRepository;
import com.example.demoapp.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    @Autowired
    UserDetailsService customUserDetailsService;

    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    NotesRepository notesRepository;

    @Autowired
    NotesService notesService;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    CustomAuthenticationHandler2 customAuthenticationHandler2;
    @Autowired
    AuthorityService authorityService;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        List<AuthenticationProvider> providers = List.of(authProvider);
        return new ProviderManager(providers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/css/**", "/jpg/**").permitAll()
                        .requestMatchers("/Login/**").permitAll()
                        .requestMatchers("/Logged/Notes/*").authenticated()
                        .requestMatchers("/Logged/Notes/DisplayNotes/SharedLinks/*").permitAll()
                        .requestMatchers("/Logged/Categories/AddCategory/**").permitAll()
                        .requestMatchers("/Logged/AdminPanel").hasAuthority("admin")
                        .requestMatchers("/Logged/Notes/**").hasAnyAuthority("admin","full-user")
                        .requestMatchers("/Logged/Categories/*").hasAnyAuthority("admin","full-user")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/Login/LoginForm")
//                        .loginPage("/Login/LoginFormValidation")
                        .defaultSuccessUrl("/Logged/MainMenu")
                        .loginProcessingUrl("/Login/LoginForm")
                        .failureUrl("/Login/LoginForm")
                        .failureHandler(customAuthenticationHandler2)
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/Login/LoginForm")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)

                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/Login/LoginForm")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler((request, response, authentication) -> {
                            categoriesService.saveCategoriesToDB();
                            categoriesService.endCategoryRepo();
                            notesService.saveNotesToDB();
                            notesService.endNotesRepo();
                            authorityService.saveCategoriesToDB();
                            authorityService.endAuthorityRepo();
                        })
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("RememberMe")
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  NoOpPasswordEncoder.getInstance();
    }
}