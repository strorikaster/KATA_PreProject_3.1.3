package com.webcrudsecurityboot.service;

import com.webcrudsecurityboot.config.EncoderConfig;
import com.webcrudsecurityboot.model.Role;
import com.webcrudsecurityboot.model.User;
import com.webcrudsecurityboot.repository.RoleRepository;
import com.webcrudsecurityboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final EncoderConfig encoderConfig;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EncoderConfig encoderConfig, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoderConfig = encoderConfig;
        this.roleRepository = roleRepository;

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
    public void save(User user, Long[] rolesId) {
        user.setPassword(encoderConfig.passwordEncoder().encode(user.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        for(Long roleId : rolesId) {
            roles.add(roleRepository.show(roleId));
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User updatedUser, Long[] rolesId) {
        if(!updatedUser.getPassword().equals(userRepository.show(updatedUser.getId()).getPassword())) {
            updatedUser.setPassword(encoderConfig.passwordEncoder().encode(updatedUser.getPassword()));
        }

        Set<Role> roles = new HashSet();
        for(Long roleId : rolesId) {
            roles.add(roleRepository.show(roleId));
        }
        updatedUser.setRoles(roles);

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
