package com.mdgspace.activityleaderboard.security.services;


import java.util.Collection;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdgspace.activityleaderboard.models.User;


public class UserDetailsImpl implements UserDetails{
    
    private  static final long serialVersionUID= 1L;
    private Long id;
    private String username;

    @Autowired
    PasswordEncoder encoder;

    @JsonIgnore
    private String accesstoken;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String accesstoken , String password){

        this.id= id;
        this.username= username;
        this.accesstoken=accesstoken;
        this.password=password;
    }

    public static UserDetailsImpl build(User user){

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getAccesstoken(),user.getPassword());
    }

    public Long getId(){
        return id;
    }

    public String getAccesstoken(){
        return accesstoken;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
    public boolean isEnabled(){
        return true;
    }

    @Override
    public boolean equals( Object o) {
        if(this==o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }





}
