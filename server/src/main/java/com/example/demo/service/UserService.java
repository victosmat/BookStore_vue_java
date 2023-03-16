package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> getUsersByKeyword(String keyword);

    List<User> getUsers(int pageNumber, int pageSize);

    User addUser(User user);

    User updateUser(User user);

    User deleteUser(Integer userID);

    User checkUser(Account account);

    int getPageNumber(int pageSize);
}
