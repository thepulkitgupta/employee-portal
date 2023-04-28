package com.tiklup.springsecurity.demo.service;

import com.tiklup.springsecurity.demo.entity.Employee;
import com.tiklup.springsecurity.demo.entity.User;
import com.tiklup.springsecurity.demo.user.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	public User findByUserName(String userName);

	public void save(CrmUser crmUser);

	public List<Employee> findAllEmployees();
}
