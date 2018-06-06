package com.votingsystem.service.impl;

import com.votingsystem.entity.User;
import com.votingsystem.repository.UserRepository;
import com.votingsystem.security.AuthUser;
import com.votingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Cacheable("users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public AuthUser loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s.toLowerCase());
        if (user != null) {
            return new AuthUser(user);
        }
        return null;
    }
}
