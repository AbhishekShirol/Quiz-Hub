//package com.quizhub.quizhub.service;
//
//import com.quizhub.quizhub.model.User;
//import com.quizhub.quizhub.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // Create User (Check Duplicates + Hash Password)
//    public User createUser(User user) {
//        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
//        }
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
//        }
//        // Hash the password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        return userRepository.save(user);
//    }
//
//    // Get All Users
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // Get User by ID
//    public User getUserById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
//    }
//
//    // Update User (Transactional to Ensure Consistency)
//    @Transactional
//    public User updateUser(Long id, User updatedUser) {
//        User existingUser = getUserById(id);
//
//        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())) {
//            if (userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
//            }
//            existingUser.setUsername(updatedUser.getUsername());
//        }
//
//        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
//            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
//            }
//            existingUser.setEmail(updatedUser.getEmail());
//        }
//
//        if (updatedUser.getPassword() != null) {
//            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Hash new password
//        }
//
//        if (updatedUser.getRole() != null) {
//            existingUser.setRole(updatedUser.getRole());
//        }
//
//        return userRepository.save(existingUser);
//    }
//
//    // Delete User (Transactional for Safety)
//    @Transactional
//    public void deleteUser(Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }
//        userRepository.deleteById(id);
//    }
//}


package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.User;
import com.quizhub.quizhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Find user by username
    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // Save or update user
    public void saveEntry(User user) {
        userRepository.save(user);
    }


    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    // Update User (Transactional to Ensure Consistency)
    @Transactional
    public User updateUser(String username, User updatedUser) {
        // Find the user by username
        User existingUser = findByUserName(username);

        // Update username if provided and different from the current one
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }

        // Update email if provided and different from the current one
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Update password if provided
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Hash new password
        }

        // Update role if provided
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        // Save the updated user
        return userRepository.save(existingUser);
    }


    // Delete User (Transactional for Safety)
    @Transactional
    public void deleteUser(String userName) {
        if (!userRepository.existsByUsername(userName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteByUsername(userName);
    }

    // Find a user by their username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
