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

import java.util.Date;

import org.springframework.beans.BeanUtils;

import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.OfyEntity;
import technology.tikal.customers.model.address.Address;
import technology.tikal.customers.model.contact.proxy.ContactProxy;
import technology.tikal.customers.model.contact.proxy.SmallContactProxy;
import technology.tikal.customers.model.media.MediaContact;
import technology.tikal.customers.model.name.IndexedByString;
import technology.tikal.customers.model.name.Name;
import technology.tikal.customers.model.name.NameFactory;
import technology.tikal.customers.model.phone.PhoneNumber;
import technology.tikal.string.util.StringNormalizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfFalse;
import com.googlecode.objectify.condition.PojoIf;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class ContactOfy implements Contact, OfyEntity<Contact> {

    public static class ContactOfyFullInfo{};
    public static class ContactOfyIndexCondition extends PojoIf<ContactOfy>{
        @Override
        public boolean matchesPojo(ContactOfy pojo) {
            return pojo.active && pojo.indexedName;
        }
    }
    @Parent @JsonIgnore
    private Key<CustomerOfy> owner;
    @Id
    private Long id;
    @Index({ContactOfyIndexCondition.class})
    @JsonIgnore
    private String normalizedName;
    
    private Name name;
    
    @Ignore @JsonIgnore
    private boolean indexedName;
    
    @Load(ContactOfyFullInfo.class) @JsonIgnore
    private Ref<ContactInfoOfy> infoRef;
    
    @Ignore @JsonIgnore
    private ContactInfoOfy transientInfo;
    
    @Index(IfFalse.class) @JsonIgnore
    private boolean active;
    
    @Index @JsonIgnore
    private Date creationTime;
    
    @Ignore @JsonIgnore
    private boolean dirty;
    
    protected ContactOfy() {
        super();
        indexedName = true;
        dirty = false;
        this.active = true;
        this.creationTime = new Date();
    }
    
    public ContactOfy(CustomerOfy owner) {
        this();
        if (owner != null && owner.getId() != null) {
            this.owner = Key.create(owner);
            this.dirty = true;
        }
    }
    
    public ContactOfy(CustomerOfy owner, Contact source) {
        this(owner);
        this.update(source);
    }
    
    public void setOwner(CustomerOfy owner) {
        if (this.owner != null) {
            if (this.owner.equivalent(Key.create(owner))) {
                return; // no hacer nada es el mismo owner
            }
            throw new IllegalArgumentException("no puede cambiar de owner");
        }
        this.owner = Key.create(owner);
        this.dirty = true;
    }
    
    private void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
        this.dirty = true;
    }
    
    public void updateInternalIndex() {
        if (!this.active) {
           this.setNormalizedName(null);
           if (name instanceof IndexedByString) {
               IndexedByString indexedName = (IndexedByString) name;
               indexedName.updateInternalIndex();
           }
        } else {
            if (this.isIndexedName()) {
                if (name == null) {
                    this.setNormalizedName(null);
                } else {
                    this.setNormalizedName(StringNormalizer.normalize(name.toString()));
                    if (name instanceof IndexedByString) {
                        IndexedByString indexedName = (IndexedByString) name;
                        indexedName.updateInternalIndex();
                    }
                }
                this.dirty = true;
            }
        }
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public Name getName() {
        return name;
    }
    @Override
    public void setName(Name param) {
        this.name = NameFactory.buildInternal(param, this.isIndexedName());
        this.dirty = true;
    }
    
    public void setInfoReference(ContactInfoOfy info) {
        if (info == null) {
            throw new IllegalArgumentException("la info no puede ser null");
        }
        if (this.infoRef == null || this.infoRef.get() == null) {
            this.infoRef = Ref.create(info);
            this.transientInfo = null;
            this.dirty = true;
        } else {
            throw new IllegalArgumentException("este objeto ya tiene una referencia");
        }
    }
    
    public boolean isIndexedName() {
        return indexedName;
    }

    public void setIndexedName(boolean indexedName) {
        this.indexedName = indexedName;
    }

    @JsonIgnore
    public ContactInfoOfy getInfoContact() {
        if (this.transientInfo != null) {
            return this.transientInfo;
        }
        return this.infoRef.get();
    }
    
    @Override
    public Address[] getAddress() {
        ContactInfoOfy info = getInfoContact();
        if (info == null) {
            return null;
        }
        return info.getAddress();
    }

    @Override
    public PhoneNumber[] getPhoneNumber() {
        ContactInfoOfy info = getInfoContact();
        if (info == null) {
            return null;
        }
        return info.getPhoneNumber();
    }

    @Override
    public MediaContact[] getMediaContact() {
        ContactInfoOfy info = getInfoContact();
        if (info == null) {
            return null;
        }
        return info.getMediaContact();
    }
    
    @Override
    public void update(Contact source) {
        if (source == null) {
            throw new IllegalArgumentException("no se puede actualizar a partir de un null");
        }
        BeanUtils.copyProperties(source, this);
        if (this.infoRef != null && this.infoRef.get() != null) {
            this.infoRef.get().update(source);
        } else {
            this.transientInfo = new ContactInfoOfy(this, source);
        }
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
    
    public boolean hasLoadedContactInfo() {
        return this.transientInfo != null || (this.infoRef != null && this.infoRef.isLoaded());
    }
    
    public ContactProxy buildProxy() {
        return new SmallContactProxy(this);
    }
}
