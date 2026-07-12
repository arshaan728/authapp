package com.example.authapp.controller;


import com.example.authapp.config.Authrequest;
import com.example.authapp.model.Admin;
import com.example.authapp.model.Consumer;
import com.example.authapp.repo.Adminrepo;
import com.example.authapp.repo.Consumerrepo;
import com.example.authapp.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class AdminController {

    @Autowired
    private Adminrepo adminrepo;
    @Autowired
    private Consumerrepo consumerrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/encodePassword")
    public void saveUserWithEncodedPassword(
            @RequestParam String username,
            @RequestParam String password, @RequestParam String role , @RequestParam(required = false) String NIC, @RequestParam(required = false) Integer number , @RequestParam(required = false) String email) {
try {

    if(role.equals("admin")) {
        Admin admin = new Admin();
        admin.setAdminusername(username);
        admin.setAdminpassword(passwordEncoder.encode(password));
        adminrepo.save(admin);
        Optional<Admin> admins = adminrepo.findByadminusername(username);
        System.out.println(admins);
    } else if (role.equals("consumer")) {
        Consumer consumer = new Consumer();
                consumer.setConsumername(username);
                consumer.setPassword(passwordEncoder.encode(password));
                consumer.setNIC(NIC);
                consumer.setNumber(number);
                consumer.setEmail(email);

                consumerrepo.save(consumer);
    }


} catch (Exception e) {
    throw new RuntimeException(e);
}

    }



    @PostMapping("/authenticate")
    public String authenticate(@RequestBody Authrequest authrequest) {
        System.out.println("in");
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authrequest.getUsername() ,
                     authrequest.getPassword()
                )
        );
        System.out.println(authentication);
        if(authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("UNKNOWN");
           return jwtService.generateToken(authrequest.getUsername() , role);
        }

        throw new BadCredentialsException("Invalid username or password");
    }
}




