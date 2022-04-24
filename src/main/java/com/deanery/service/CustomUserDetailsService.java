package com.deanery.service;

import com.deanery.entity.User;
import com.deanery.exception.UserAlreadyExistsException;
import com.deanery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String userName, String password, List<String> roles) throws UserAlreadyExistsException {

        if (userRepository.findUserByUserName(userName).isPresent()) {
            throw new UserAlreadyExistsException("User " + userName + " already exists");
        }

        userRepository.save(new User(userName, password, roles));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
