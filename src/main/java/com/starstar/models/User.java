package com.starstar.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
@Document
public class User implements UserDetails {
    public static final String ROLE_ADMIN="ROLE_ADMIN";
    public static final String ROLE_USER="ROLE_USER";
    @Id
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Indexed(unique = true)
    private String username;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isEnabled;
    private List<Role> authorities;
    private boolean isCredentialsNonExpired;
    private String first;
    private String last;
    private String avatar;
    private String email;
    private List<String> phones;

    public User(Builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.username = builder.username;
        this.isAccountNonExpired = builder.isAccountNonExpired;
        this.isAccountNonLocked = builder.isAccountNonLocked;
        this.isEnabled = builder.isEnabled;
        this.authorities = builder.roles;
        this.isCredentialsNonExpired = builder.isCredentialsNonExpired;
        this.first = builder.first;
        this.last = builder.last;
        this.avatar = builder.avatar;
        this.phones = builder.phones;
        this.email=builder.email;
    }

    public User() {

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsAccountNonExpired(Boolean isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }

    public void setIsAccountNonLocked(Boolean isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setRoles(List<Role> roles) {
        this.authorities = roles;
    }

    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        for (Role role : authorities) {
            auths.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return auths;
    }
   public boolean hasRole(String role){
        for(Role r:authorities){
            if(r.getAuthority().equals(role)){
                return true;
            }
        }
        return false;
   }
    public void removeRole(String role){
       Role ro=null;
        for(Role r:authorities){
            if(r.getAuthority().equals(role)){
               ro=r;
            }
        }
        if(ro!=null){
            authorities.remove(ro);
        }
    }
    public void addRole(String role){

            authorities.add(new Role(role));

    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public static class Builder {
        private String id;
        private String password;
        private String username;
        private Boolean isAccountNonExpired;
        private Boolean isAccountNonLocked;
        private Boolean isEnabled;
        private boolean isCredentialsNonExpired;
        private String first;
        private String last;
        private String avatar;
        private List<Role> roles;
        private List<String> phones;



        private String email;

        public Builder() {

        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder accountNonExpired(Boolean accountNonExpired) {
            isAccountNonExpired = accountNonExpired;
            return this;
        }

        public Builder accountNonLocked(Boolean accountNonLocked) {
            isAccountNonLocked = accountNonLocked;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            isEnabled = enabled;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            isCredentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder first(String first) {
            this.first = first;
            return this;
        }

        public Builder last(String last) {
            this.last = last;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder phones(List<String> phones) {
            this.phones = phones;
            return this;
        }

        public User build() {
            return new User(this);
        }


    }
}
