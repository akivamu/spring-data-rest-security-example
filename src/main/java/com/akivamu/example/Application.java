package com.akivamu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    @Autowired
    LaptopRepository laptopRepository;
    @Autowired
    MeetingRoomRepository meetingRoomRepository;
    @Autowired
    WcRepository wcRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @PostConstruct
    public void init() {
        SecurityUtils.runAs("system", "system", "ROLE_MANAGER", "ROLE_EMPLOYEE");

        wcRepository.save(new Wc("3F", "WC 3th Floor"));
        wcRepository.save(new Wc("4F", "WC 4th Floor"));

        meetingRoomRepository.save(new MeetingRoom("3F1", "3th Floor - Room 1"));
        meetingRoomRepository.save(new MeetingRoom("3F2", "3th Floor - Room 2"));
        meetingRoomRepository.save(new MeetingRoom("4F1", "4th Floor - Room 1"));

        laptopRepository.save(new Laptop("1234", "Bob"));
        laptopRepository.save(new Laptop("2222", "Alice"));

        SecurityContextHolder.clearContext();
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @EnableWebSecurity
    static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().
                    withUser("Bob").password("Bob").roles("EMPLOYEE").and().
                    withUser("Alice").password("Alice").roles("EMPLOYEE", "MANAGER");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.httpBasic()
                    .and().authorizeRequests()

                    // Don't allow anyone to create/delete WCs
                    .antMatchers(HttpMethod.POST, "/wcs").denyAll()
                    .antMatchers(HttpMethod.DELETE, "/wcs").denyAll()

                    // Only MANAGERs can create/delete meeting rooms
                    .antMatchers(HttpMethod.POST, "/meetingRooms").hasRole("MANAGER")
                    .antMatchers(HttpMethod.DELETE, "/meetingRooms").hasRole("MANAGER")

                    // Only MANAGERs can create laptops
                    .antMatchers(HttpMethod.POST, "/laptops").hasRole("MANAGER")

                    // Only EMPLOYEEs can delete `his own` laptops
                    // Here only check ROLE, specific (`his own` term) will be checked in repo declaration
                    .antMatchers(HttpMethod.DELETE, "/laptops").hasRole("EMPLOYEE")

                    .and().csrf().disable();
        }
    }
}
