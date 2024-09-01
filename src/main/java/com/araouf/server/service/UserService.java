package com.araouf.server.service;


import org.springframework.data.domain.Page;

import com.araouf.server.domain.db.User;

public interface UserService {

    Page<User> getAllUsers(int page, int size,String search);

    User getById(Long id);

    User getMe(String email);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);
}
