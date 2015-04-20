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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Parent;

import technology.tikal.customers.model.OfyEntity;
import technology.tikal.customers.model.address.Address;
import technology.tikal.customers.model.address.AddressFactory;
import technology.tikal.customers.model.media.MediaContact;
import technology.tikal.customers.model.media.MediaContactFactory;
import technology.tikal.customers.model.phone.PhoneNumber;
import technology.tikal.customers.model.phone.PhoneNumberFactory;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class ContactInfoOfy implements OfyEntity<Contact>{

    @Parent @JsonIgnore
    private Key<ContactOfy> owner;
    @Id
    private Long id;
    private List<Address> address;
    private List<PhoneNumber> phoneNumber;
    private List<MediaContact> mediaContact;
    @Ignore @JsonIgnore
    private boolean dirty;
    
    private ContactInfoOfy() {
        this.address = new ArrayList<>();
        this.phoneNumber = new ArrayList<>();
        this.mediaContact = new ArrayList<>();
        this.dirty = false;
    }
    
    public ContactInfoOfy (ContactOfy owner) {
        this();
        if (owner != null && owner.getId() != null) {
            this.owner = Key.create(owner);
            this.dirty = true;
        }
    }
    
    public ContactInfoOfy(ContactOfy owner, Contact source) {
        this(owner);
        this.update(source);
    }
    
    public void setOwner(ContactOfy owner) {
        if (this.owner != null) {
            throw new IllegalArgumentException("no puede cambiar de owner");
        }
        this.owner = Key.create(owner);
        this.dirty = true;
    }
    
    public Long getId() {
        return id;
    }
    
    public Address[] getAddress() {
        Address[] r = address.toArray(new Address[address.size()]);
        return r;
    }

    public PhoneNumber[] getPhoneNumber() {
        PhoneNumber[] r = phoneNumber.toArray(new PhoneNumber[phoneNumber.size()]);
        return r;
    }

    public MediaContact[] getMediaContact() {
        MediaContact[] r = mediaContact.toArray(new MediaContact[mediaContact.size()]);
        return r;
    }
    
    public void update(Contact source) {
        address.clear();
        if (source.getAddress() != null) {
            for(Address x: source.getAddress()) {
                if (x != null) {
                    address.add(AddressFactory.buildInternal(x));
                }
            }
        }
        phoneNumber.clear();
        if (source.getPhoneNumber() != null) {
            for(PhoneNumber x: source.getPhoneNumber()) {
                if (x != null) {
                    phoneNumber.add(PhoneNumberFactory.buildInternal(x));
                }
            }
        }
        mediaContact.clear();
        if (source.getMediaContact() != null) {
            for(MediaContact x: source.getMediaContact()) {
                if (x != null) {
                    mediaContact.add(MediaContactFactory.buildInternal(x));
                }
            }
        }
        this.dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }
    
    public void clean() {
        dirty = false;
    }
}
