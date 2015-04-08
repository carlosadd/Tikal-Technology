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
package technology.tikal.gae.security.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author Nekorp
 *
 */
public class InternalGrantedAuthority implements GrantedAuthority {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @NotNull
    @Length(min=8, max=40)
    @Pattern(regexp="^\\w+((-_)\\w+)*")
    private String authority;    
    private InternalGrantedAuthority() {
    }
    
    public InternalGrantedAuthority(String authority) {
        this();
        if (StringUtils.isEmpty(authority)) {
            throw new IllegalArgumentException("Authority cannot be empty");
        }
        this.authority = authority;
    }
    public String getAuthority() {
        return authority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((authority == null) ? 0 : authority.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InternalGrantedAuthority other = (InternalGrantedAuthority) obj;
        if (authority == null) {
            if (other.authority != null)
                return false;
        } else if (!authority.equals(other.authority))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return authority;
    }
}
