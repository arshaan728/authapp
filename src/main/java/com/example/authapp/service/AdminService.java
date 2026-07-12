package com.example.authapp.service;

import com.example.authapp.model.Admin;
import com.example.authapp.model.Consumer;
import com.example.authapp.repo.Adminrepo;

import com.example.authapp.repo.Consumerrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collections;
import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private Adminrepo adminrepo;

    @Autowired
    private Consumerrepo consumerrepo;

    public Admin getuserFromusername(String username) {
        Optional<Admin> adminOptional = adminrepo.findByadminusername(username);

        return adminOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username));

    }
    public Consumer getConsumerFromUsername(String username) {
        Optional<Consumer> consumerOptional = consumerrepo.findByconsumerusername(username);
        return consumerOptional.orElseThrow(() ->
                new UsernameNotFoundException("Consumer not found: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Admin> adminOptional = adminrepo.findByadminusername(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return User.builder()
                    .username(admin.getAdminusername())
                    .password(admin.getAdminpassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        }

        Optional<Consumer> consumerOptional = consumerrepo.findByconsumerusername(username);
        if (consumerOptional.isPresent()) {
            Consumer consumer = consumerOptional.get();
            return User.builder()
                    .username(consumer.getConsumername())
                    .password(consumer.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_CONSUMER")))
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }


}
