package com.webcrudsecurityboot.service;

import com.webcrudsecurityboot.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User show(Long id);
    void save(User user);
    void save(User user, Long[] rolesId);
    //void update(User updatedUser);
    void update(User updatedUser, Long[] rolesId);
    void delete(Long id);
    UserDetails loadUserByUsername(String email);
}
