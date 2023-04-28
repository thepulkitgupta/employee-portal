package com.tiklup.springsecurity.demo.repository.Security;

import com.tiklup.springsecurity.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    public User findByUserNameContainsAllIgnoreCase(String userName);
}
