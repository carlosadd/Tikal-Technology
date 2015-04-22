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

import technology.tikal.string.util.StringNormalizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class ContactRelationshipOfy implements ContactRelationship {

    @Parent @JsonIgnore
    private Key<ContactOfy> owner;
    
    @Id
    private Long id;
    
    @Index @JsonIgnore
    private String normalizedRelationship;
    
    private String relationship;
    
    @JsonIgnore @Load
    private Ref<ContactOfy> contactRef;
    
    private ContactRelationshipOfy() {
    }
    
    public ContactRelationshipOfy(ContactOfy owner, String relationship, ContactOfy contact) {
        this();
        if (owner == null || owner.getId() == null) {
            throw new IllegalArgumentException("owner no valido");
        }
        if (StringUtils.isEmpty(relationship)) {
            throw new IllegalArgumentException("relationship invalid");
        }
        this.owner = Key.create(owner);
        this.setContact(contact);
        this.relationship = relationship;
        this.normalizedRelationship = StringNormalizer.normalize(relationship);
    }
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getRelationship() {
        return relationship;
    }
    
    public String getNormalizedRelationship() {
        return normalizedRelationship;
    }

    @Override
    public Contact getContact() {
        return contactRef.get();
    }

    public void setRelationship(String relationship) {
        if (StringUtils.isEmpty(relationship)) {
            throw new IllegalArgumentException("relationship invalid");
        }
        this.relationship = relationship;
        this.normalizedRelationship = StringNormalizer.normalize(relationship);
    }

    public void setContact(ContactOfy contact) {
        if (contact == null || contact.getId() == null) {
            throw new IllegalArgumentException("contact no valido");
        }
        this.contactRef = Ref.create(contact);
    }
}
