package com.example.demobatis2.config;

import com.example.demobatis2.entity.Role;
import com.example.demobatis2.entity.UserCustom;
import com.example.demobatis2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserCustom userCustom = userMapper.findByUsername(s);
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (Role role : userCustom.getRoles()) {
                    grantedAuthorities.add(
                            new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
                }
                return grantedAuthorities;
            }

            @Override
            public String getPassword() {
                return userCustom.getPassword();
            }

            @Override
            public String getUsername() {
                return userCustom.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return userCustom.getIsActive();
            }

            @Override
            public boolean isAccountNonLocked() {
                return userCustom.getIsActive();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return userCustom.getIsActive();
            }

            @Override
            public boolean isEnabled() {
                return userCustom.getIsActive();
            }
        };

        return userDetails;
    }
}
