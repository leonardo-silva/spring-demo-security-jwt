package com.demo.security.jwt.security;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServlet;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@EnableGlobalMethodSecurity(prePostEnabled = true) is deprecated, use @EnableMethodSecurity(prePostEnabled = true) instead
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.headers().frameOptions().disable();
//        http.cors().and().csrf().disable()
//                .addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .antMatchers(SWAGGER_WHITELIST).permitAll()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers(HttpMethod.POST,"/login").permitAll()
//                .antMatchers(HttpMethod.POST,"/users").permitAll()
//                .antMatchers(HttpMethod.GET,"/users").hasAnyRole("USERS","MANAGERS")
//                .antMatchers("/managers").hasAnyRole("MANAGERS")
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers(HttpMethod.GET,"/users").hasAnyRole("USERS","MANAGERS")
                        .requestMatchers("/managers").hasAnyRole("MANAGERS")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        //http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean //HABILITANDO ACESSAR O H2-DATABASE NA WEB
    public ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/h2-console/*");
        return registrationBean;
    }
}