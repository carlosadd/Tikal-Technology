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
    private List<Address> address;
    private List<PhoneNumber> phoneNumber;
    private List<MediaContact> mediaContact;
    
    public ContactPojo() {
        this.address = new ArrayList<>();
        this.phoneNumber = new ArrayList<>();
        this.mediaContact = new ArrayList<>();
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
    public List<Address> getAddress() {
        return address;
    }
    
    @Override
    public List<PhoneNumber> getPhoneNumber() {
        return phoneNumber;
    }
    
    @Override
    public List<MediaContact> getMediaContact() {
        return mediaContact;
    }
    
}
