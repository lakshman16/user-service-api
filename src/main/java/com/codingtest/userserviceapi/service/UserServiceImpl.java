package com.codingtest.userserviceapi.service;

import com.codingtest.userserviceapi.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static List<User> users;

    static {
        users = loadDummyUsers();
    }

    private static List<User> loadDummyUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1234567890, "John", "Doe", "john.doe@gmail.com"));
        users.add(new User(1234567891, "Will", "Smith", "will.smith@yahoo.com"));
        users.add(new User(1234567892, "Christian", "Bale", "christian.bale@live.com"));
        users.add(new User(1234567893, "Keanu", "Reeves", "keanu.reeves@msn.com"));
        users.add(new User(1234567894, "Heath", "Ledger", "heath.ledger@hotmail.com"));
        return users;
    }

    @Override
    public boolean saveUser(User user) {
        users.add(user);
        return true;
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public User findUserBySsn(long ssn) {
        return findUser(ssn);
    }

    @Override
    public boolean deleteUserBySsn(long ssn) {
        if(isUserExists(ssn)) {
            deleteExistingUserFromUsers(ssn);
        }
        return true;
    }

    @Override
    public boolean isUserExists(long ssn) {
        boolean isUserExists = false;
        if(Optional.ofNullable(users).isPresent()) {
            User existingUser = users.stream().filter(u -> (ssn == u.getSsn()))
                    .findAny()
                    .orElse(null);
            isUserExists = Optional.ofNullable(existingUser).isPresent();
        }
        return isUserExists;
    }

    private User findUser(long ssn) {
        User user = null;
        if(Optional.ofNullable(users).isPresent()) {
            user = users.stream().filter(u -> (u.getSsn() == ssn))
                    .findAny()
                    .orElse(null);
        }
        return user;
    }

    private void deleteExistingUserFromUsers(long ssn) {
        if(Optional.ofNullable(users).isPresent()) {
            users = users.stream().filter(u -> (ssn != u.getSsn()))
                    .collect(Collectors.toList());
        }
    }
}
