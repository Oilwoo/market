package com.example.market.service;

import com.example.market.model.ShoppingCart;
import com.example.market.model.User;
import com.example.market.repository.ShoppingCartRepository;
import com.example.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public UserService(UserRepository userRepository, ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public User registerUser(User user) {
        //해당 유저의 쇼핑카트 만들어주기
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        shoppingCartRepository.save(newCart);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        // For the password, you might want to check if it's non-empty before updating it.
        // And seriously consider encoding it in a real application.
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // You can implement the findByUsername method here if it's used in your controller for login or other purposes.
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

// Other
