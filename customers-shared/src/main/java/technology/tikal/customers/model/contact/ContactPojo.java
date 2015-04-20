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

import com.fasterxml.jackson.annotation.JsonTypeName;
import technology.tikal.customers.model.address.Address;
import technology.tikal.customers.model.media.MediaContact;
import technology.tikal.customers.model.name.Name;
import technology.tikal.customers.model.phone.PhoneNumber;

/**
 * 
 * @author Nekorp
 *
 */
@JsonTypeName("Contact")
public class ContactPojo implements Contact {

    private Long id;
    private Name name;
    private Address[] address;
    private PhoneNumber[] phoneNumber;
    private MediaContact[] mediaContact;
    
    public ContactPojo() {
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public Name getName() {
        return name;
    }
    @Override
    public void setName(Name name) {
        this.name = name;
    }
    
    @Override
    public Address[] getAddress() {
        return address;
    }
    
    public void setAddress(Address[] address) {
        this.address = address;
    }
    
    @Override
    public PhoneNumber[] getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(PhoneNumber[] phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public MediaContact[] getMediaContact() {
        return mediaContact;
    }

    public void setMediaContact(MediaContact[] mediaContact) {
        this.mediaContact = mediaContact;
    }
    
}
