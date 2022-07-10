package com.example.project_2th.security.service;

import com.example.project_2th.entity.User;
import com.example.project_2th.exception.PostNotFound;
import com.example.project_2th.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(Long.valueOf(username)).orElseThrow(PostNotFound::new);

        List<GrantedAuthority> roles= new ArrayList<>();

        roles.add(new SimpleGrantedAuthority(user.getRole()));

        UserContext userContext = new UserContext(user,roles);

        return userContext;
    }
}
