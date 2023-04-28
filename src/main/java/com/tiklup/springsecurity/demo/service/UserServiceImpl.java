package com.tiklup.springsecurity.demo.service;


import com.tiklup.springsecurity.demo.entity.Employee;
import com.tiklup.springsecurity.demo.entity.Role;
import com.tiklup.springsecurity.demo.entity.User;
import com.tiklup.springsecurity.demo.repository.Employees.EmployeeRepository;
import com.tiklup.springsecurity.demo.repository.Security.RoleRepository;
import com.tiklup.springsecurity.demo.repository.Security.UserRepository;
import com.tiklup.springsecurity.demo.user.CrmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private EmployeeRepository employeeRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository1, RoleRepository roleRepository1, EmployeeRepository theEmployeeRepository){
		userRepository=userRepository1;
		roleRepository=roleRepository1;
		employeeRepository = theEmployeeRepository;
	}

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	@Transactional("securityTransactionManager")
	public User findByUserName(String userName) {
		// check the database if the user already exists
		return userRepository.findByUserNameContainsAllIgnoreCase(userName);
	}

	@Override
	@Transactional("securityTransactionManager")
	public void save(CrmUser crmUser) {
		User user = new User();
		 // assign user details to the user object
		user.setUserName(crmUser.getUserName());
		user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
		user.setFirstName(crmUser.getFirstName());
		user.setLastName(crmUser.getLastName());
		user.setEmail(crmUser.getEmail());

		// give user default role of "employee"
		user.setRoles(Arrays.asList(roleRepository.findByNameContainsAllIgnoreCase("ROLE_EMPLOYEE")));

		 // save user in the database
		userRepository.save(user);
	}

	@Override
	@Transactional("securityTransactionManager")
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserNameContainsAllIgnoreCase(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional("employeesTransactionManager")
	public List<Employee> findAllEmployees() {
		return employeeRepository.findAllByOrderByLastNameAsc();
	}
}
