package com.example.demo.service.Impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.payLoad.dto.AuthDTO;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CartRepository cartRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private WebClient webClient;

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public List<User> getUsersByKeyword(String keyword) {
        return userRepository.findUserByKeywords("%" + keyword + "%");
    }

    @Override
    public List<User> getUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findUsers(pageable);
    }

    @Override
    public User addUser(User user) {
        if (accountRepository.getAccountByUsername(user.getAccount().getUsername()) == null) {
            user = userRepository.save(user);
            // tạo giỏ hàng mới
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (accountRepository.getAccountByUsername(user.getAccount().getUsername()) == null) {
            User user1 = userRepository.findById(user.getId()).get();
            user1.setName(user.getName());
            user1.setPhoneNumber(user.getPhoneNumber());
            user1.setEmail(user.getEmail());
            user1.setGender(user.getGender());
            user1.setPosition(user.getPosition());
            user1.setDateOfBirth(user.getDateOfBirth());
            user1.setAccount(user.getAccount());
            user = userRepository.save(user1);
        } else user.getAccount().setUsername("already exist");
        return user;
    }

    @Override
    public User deleteUser(Integer userID) {
        userRepository.deleteById(userID);
        return userRepository.findById(userID).get();
    }

    @Override
    public int getPageNumber(int pageSize) {
        List<User> users = userRepository.findAll();
        return (int) Math.ceil((float) users.size() / pageSize);
    }

    @Override
    public AuthDTO validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token))
            throw new RuntimeException("Invalid JWT token");

        Optional<User> userOptional = userRepository.findById(jwtTokenProvider.getUserIdFromJWT(token));

        if (userOptional.isEmpty()) {
//            throw new AppException("User not found", HttpStatus.NOT_FOUND);
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        return new AuthDTO(user.getId(), user.getAccount().getUsername(), token);
    }
}
