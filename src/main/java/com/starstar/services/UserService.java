package com.starstar.services;

import com.starstar.models.User;
import com.starstar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UsernameNotFoundException("username");
        return user;
    }
    public User loadUserByUserId(String id) throws Exception {
        User user = userRepository.findOne(id);
        if(user == null) throw new Exception("Id not exists");
        return user;
    }

    public User save(User user ) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    } }