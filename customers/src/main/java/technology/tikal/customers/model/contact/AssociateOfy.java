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
package technology.tikal.customers.model.contact;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.contact.proxy.ContactProxy;
import technology.tikal.customers.model.contact.proxy.SmallAssociateProxy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.string.util.StringNormalizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.condition.IfNotEmpty;
/**
 * @author Nekorp
 * El simple hecho de ser de tipo associated y ser hija de customer es la informacion suficiente para 
 * crear la lista de asociados!
 *
 */
@Subclass
public class AssociateOfy extends ContactOfy implements Associate {
    @Index({IfNotEmpty.class}) @JsonIgnore
    private String normalizedRole;
    private String role;
    protected AssociateOfy() {
        super();
    }
    
    public AssociateOfy(CustomerOfy owner, Associate source) {
        super(owner);
        this.update(source);
    }
    
    public void setRole(String role) {
        if (StringUtils.isEmpty(role)) {
            throw new IllegalArgumentException("El rol no puede estar vacio");
        }
        this.role = role;
    }

    public String getRole() {
        return role;
    }
    
    @Override
    public void updateInternalIndex() {
        if(this.isActive()) {
            this.normalizedRole = StringNormalizer.normalize(role);
        } else {
            this.normalizedRole = null;
        }
        super.updateInternalIndex();
    }

    @Override
    public void update(Contact source) {
        super.update(source);
        if (source instanceof Associate) {
            Associate associate = (Associate) source;
            this.setRole(associate.getRole());
        } else {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotSupportedClass.AssociateOfy.update"}, 
                    new String[]{source.getClass().getSimpleName()}, 
                    "Associate object expected"));
        }
    }
    
    @Override
    public ContactProxy buildProxy() {
        return new SmallAssociateProxy(this);
    }
}
