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
package technology.tikal.customers.model.contact.proxy;

import technology.tikal.customers.model.contact.Associate;
import technology.tikal.customers.model.contact.Contact;
import technology.tikal.customers.model.contact.ContactRelationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Nekorp
 *
 */
public class SmallContactRelationshipProxy implements ContactRelationship {

    @JsonIgnore
    private Contact contactProxy;
    @JsonIgnore
    private ContactRelationship delegate;
    
    public SmallContactRelationshipProxy(ContactRelationship delegate) {
        this.delegate = delegate;
        if (delegate.getContact() instanceof Associate) {
            Associate associate = (Associate) delegate.getContact();
            this.contactProxy = new SmallAssociateProxy(associate);
        } else {
            this.contactProxy = new SmallContactProxy(delegate.getContact());
        }
    }
    @Override
    public Long getId() {
        return delegate.getId();
    }
    @Override
    public String getRelationship() {
        return delegate.getRelationship();
    }
    @Override
    public Contact getContact() {
        return contactProxy;
    }
}
