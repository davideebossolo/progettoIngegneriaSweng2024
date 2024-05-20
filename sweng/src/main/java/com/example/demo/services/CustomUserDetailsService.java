package com.example.demo.services;

import com.example.demo.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    //pass an object of userRepository
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw  new UsernameNotFoundException("user not found");
        }
        return new CustomUserDetails(user);
    }

    // Aggiungi un metodo per verificare l'esistenza di un utente con un determinato firstName
    public boolean existsUserByFirstName(String firstName) {
        return userRepository.existsByFirstName(firstName);
    }

}
