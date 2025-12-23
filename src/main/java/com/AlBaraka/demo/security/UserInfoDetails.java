package com.AlBaraka.demo.security;


import com.AlBaraka.demo.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean active;
    @Getter
    private final Long id ;
    @Getter
    private User u ;




    public UserInfoDetails(User user) {
        this.id = user.getId();
        this.u = user ;
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();


        this.authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


}
