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
package technology.tikal.customers.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import technology.tikal.customers.model.contact.ContactOfy.ContactOfyFullInfo;
import technology.tikal.customers.model.contact.PrimaryContact;
import technology.tikal.customers.model.contact.PrimaryContactOfy;
import technology.tikal.customers.model.name.IndexedByString;
import technology.tikal.customers.model.name.Name;
import technology.tikal.customers.model.name.NameFactory;
import technology.tikal.customers.model.proxy.CustomerProxy;
import technology.tikal.customers.model.proxy.SmallCustomerProxy;
import technology.tikal.string.util.StringNormalizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.condition.IfFalse;
import com.googlecode.objectify.condition.PojoIf;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class CustomerOfy implements Customer, OfyEntity<Customer> {
    public static class CustomerOfyFull extends ContactOfyFullInfo{}
    private static class ActiveCustomerCheck extends PojoIf<CustomerOfy> {
        @Override
        public boolean matchesPojo(CustomerOfy pojo) {
            return pojo.active;
        }
    }
    @Id
    private Long id;
    
    private Name name;
    
    private Group group;
    
    @Index(ActiveCustomerCheck.class) 
    @JsonIgnore
    private String normalizedName;
    
    @Load(CustomerOfyFull.class) @JsonIgnore
    private Ref<PrimaryContactOfy> primaryContactRef;
    
    @Ignore @JsonIgnore
    private PrimaryContactOfy transientInfo;
    
    @Index(IfFalse.class) 
    @JsonIgnore
    private boolean active;
    
    @Index(ActiveCustomerCheck.class) @JsonIgnore
    private Date creationTime;
    
    @Ignore @JsonIgnore
    private boolean dirty;
    
    protected CustomerOfy() {
        super();
        dirty = false;
        this.active = true;
        this.creationTime = new Date();
    }
    
    protected CustomerOfy(Long id) {
        this();
        this.id = id;
    }
    
    public CustomerOfy(Customer source) {
        this();
        this.update(source);
    }
    
    public CustomerOfy(Long id, Customer source) {
        this(id);
        this.update(source);
    }
    
    @Override
    public Long getId() {
        return id;
    }
    
    @Override
    public void setName(Name param) {
        if(param == null) {
            throw new NullPointerException("El nombre no puede ser nulo");
        }
        Name name = NameFactory.buildInternal(param, true); 
        if (StringUtils.isEmpty(name.toString())) {
            throw new IllegalArgumentException("el nombre no puede estar vacio");
        }
        this.name = name;
        dirty = true;
    }

    @Override
    public Name getName() {
        return name;
    }
    
    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public void setGroup(Group source) {
        this.group = GroupFactory.buildInternal(source);
        dirty = true;
    }

    @Override
    public PrimaryContact getPrimaryContact() {
        if (this.transientInfo != null) {
            return this.transientInfo;
        }
        return this.primaryContactRef.get();
    }
    
    @Override
    public void update(Customer source) {
        BeanUtils.copyProperties(source, this);
        if (this.primaryContactRef != null && this.primaryContactRef.get() != null) {
            this.primaryContactRef.get().update(source.getPrimaryContact());
        } else {
            this.transientInfo = new PrimaryContactOfy(this, source.getPrimaryContact());
        }
    }
    
    private void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
    
    public void updateInternalIndex() {
        this.setNormalizedName(StringNormalizer.normalize(name.toString()));
        if (name instanceof IndexedByString) {
            IndexedByString indexedName = (IndexedByString) name;
            indexedName.updateInternalIndex();
        }
    }

    public void setContactReference(PrimaryContactOfy info) {
        if (info == null) {
            throw new IllegalArgumentException("la info no puede ser null");
        }
        if (this.primaryContactRef == null || this.primaryContactRef.get() == null) {
            this.primaryContactRef = Ref.create(info);
            this.transientInfo = null;
        } else {
            throw new IllegalArgumentException("este objeto ya tiene una referencia");
        }
        dirty = true;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (this.name instanceof IndexedByString) {
            IndexedByString indexedName = (IndexedByString) name;
            indexedName.setActive(active);
        }
        dirty = true;
    }

    public Date getCreationTime() {
        return creationTime;
    }
    
    public boolean isDirty() {
        return dirty;
    }
    
    public void clean() {
        this.dirty = false;
    }

    public boolean hasLoadedContact() {
        return this.transientInfo != null || (this.primaryContactRef != null && this.primaryContactRef.isLoaded());
    }
    
    public CustomerProxy buildProxy() {
        return new SmallCustomerProxy(this);
    }
}
