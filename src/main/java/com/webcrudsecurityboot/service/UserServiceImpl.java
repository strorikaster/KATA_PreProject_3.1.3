package com.webcrudsecurityboot.service;

import com.webcrudsecurityboot.config.EncoderConfig;
import com.webcrudsecurityboot.model.User;
import com.webcrudsecurityboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final EncoderConfig encoderConfig;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EncoderConfig encoderConfig) {
        this.userRepository = userRepository;
        this.encoderConfig = encoderConfig;

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User show(Long id) {
        return userRepository.show(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(encoderConfig.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User updatedUser) {
        if(!updatedUser.getPassword().equals(userRepository.show(updatedUser.getId()).getPassword())) {
            updatedUser.setPassword(encoderConfig.passwordEncoder().encode(updatedUser.getPassword()));
        }
        userRepository.update(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByName(email);
        if(user == null) {
            throw new UsernameNotFoundException("User " + email + " not found!");

        }
        return user;
    }


}
