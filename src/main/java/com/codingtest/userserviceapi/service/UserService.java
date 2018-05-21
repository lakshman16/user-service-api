package com.codingtest.userserviceapi.service;

import com.codingtest.userserviceapi.model.User;

import java.util.List;

public interface UserService {
    boolean saveUser(User user);
    List<User> findAllUsers();
    User findUserBySsn(long ssn);
    boolean deleteUserBySsn(long ssn);
    boolean isUserExists(long ssn);
}
