package com.work.auth.service;

import com.work.auth.model.AuthUser;
import com.work.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthUserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    AuthUser user =
        repository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + email));

    return User.builder().username(user.getEmail()).password(user.getPassword()).build();
  }
}
