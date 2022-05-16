package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    //методы CRUD

    public User add(User user) {
        return userRepository.save(user);
    }


    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }


    public User update(User user) {
        return userRepository.save(user);
    }


    public User getUserById(long id) {
        return userRepository.getById(id);
    }


    public List<User> listOfAllUsers() {
        return userRepository.findAll();
    }


    public User getUserByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @PostConstruct
    @Transactional

    void AddUsers() {

        Role role1 = new Role(1L, "ROLE_ADMIN");
        Role role2 = new Role(2L, "ROLE_USER");

        userRepository.save(new User(1L, "admin@admin.ru", "$2a$12$5/LJTcY5BTW/1dfKXfkONu9SiZbiZfAy2B41V2MuIVrimLrY4ew0K", "admin", 32))
                .setRoles(Collections.singleton(role1));
        userRepository.save(new User(2L, "user@user.ru", "$2a$12$T3fRruYVddVDEv/6kbYOhuKSMp4YKx/YvjYiYvtL.QjiEF2rNIZuy", "user", 68))
                .setRoles(Collections.singleton(role2));


    }
}


