/**
 *   Copyright 2015 Tikal-Technology
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */
package technology.tikal.gae.system.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.userdetails.UserDetails;

import technology.tikal.gae.security.model.InternalGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class SystemUser implements UserDetails {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Length(min=8, max=26)
    @Pattern(regexp="\\w*")
    private String username;
    
    @NotNull
    @Length(min=8, max=26)
    @Pattern(regexp="\\w*")
    private String password;
    
    private List<InternalGrantedAuthority> authorities;
    
    private SystemUser() {
        authorities = new ArrayList<>(); 
    }
    
    public SystemUser(String username) {
        this();
        this.username = username;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public Collection<InternalGrantedAuthority> getAuthorities() {
        return authorities;
    }    
    
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void update(SystemUser origin) {
        this.setPassword(origin.getPassword());
        this.authorities.clear();
        this.authorities.addAll(origin.getAuthorities());
    }
}
