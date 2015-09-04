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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import technology.tikal.customers.model.contact.PrimaryContact;
import technology.tikal.customers.model.name.Name;

/**
 * 
 * @author Nekorp
 *
 */
@JsonSubTypes({
    @JsonSubTypes.Type(value = Customer.class),
    @JsonSubTypes.Type(value = ClienteMx.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonDeserialize(as=CustomerPojo.class)
@JsonTypeName("Customer")
public interface Customer {
    
    public Long getId();
    
    @NotNull @Valid
    public Name getName();
    
    public void setName(Name name);
    
    public Group getGroup();
    
    public void setGroup(Group group);

    @Valid
    public PrimaryContact getPrimaryContact();
    
}
