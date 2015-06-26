package technology.tikal.gae.system.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import technology.tikal.gae.security.model.InternalGrantedAuthority;

@Entity
@Cache
public class UserSession implements UserDetails {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @NotBlank
    private String username;
    @NotBlank
    private String token;
    @Valid
    private List<InternalGrantedAuthority> authorities;
    private String authenticationMethod;
    @JsonIgnore
    private Date creationTime;
    
    private UserSession() {
        this.authorities = new ArrayList<>();
        this.creationTime = new Date();
    }
    
    public UserSession(String username, String token) {
        this();
        this.username = username;
        this.token = token;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<InternalGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return token;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public void setAuthenticationMethod(String authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    public Date getCreationTime() {
        return creationTime;
    }

}
