package org.example.spring.config.security;

import org.example.spring.dao.AuthUserDao;
import org.example.spring.domain.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthUserDao authUserDao;

    public CustomUserDetailsService(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found {%s}".formatted(username))
        );
        String role = "ROLE_" + authUser.getRole();

        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
        return new CustomUserDetails(authUser, authorities);
    }


}
