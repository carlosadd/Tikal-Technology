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
package technology.tikal.customers.controller;

import java.util.List;

import technology.tikal.customers.dao.filter.ContactFilterSmall;
import technology.tikal.customers.dao.filter.ContactRelationshipFilter;
import technology.tikal.customers.dao.filter.CustomerFilterSmall;
import technology.tikal.customers.model.Customer;
import technology.tikal.customers.model.contact.Contact;
import technology.tikal.customers.model.contact.ContactRelationship;
import technology.tikal.gae.pagination.model.PaginationDataDualLongString;

/**
 * 
 * @author Nekorp
 *
 */
public interface CustomersController {

    //CRUD CUSTOMER
    
    Customer createCustomer(Customer data);
    
    Customer createCustomerWithForcedId(Long id, Customer data);
    
    void updateCustomer(Long customerId, Customer data);
    
    void deactivateCustomer(Long customerId);
    
    Customer[] queryCustomers(CustomerFilterSmall filter, PaginationDataDualLongString pagination);
    
    Customer getCustomer(Long customerId);
    
    //CRUD CONTACT
    
    Contact createContact(Long customerId, Contact data);
    
    void updateContact(Long customerId, Long contactId, Contact data);
    
    void deactivateContact(Long customerId, Long contactId);
    
    Contact[] queryContact(Long customerId, ContactFilterSmall filter, PaginationDataDualLongString pagination);
    
    Contact getContact(Long customerId, Long contactId);
    
    //CRUD RELATIONSHIP

    ContactRelationship createRelation(Long customerId, Long contactId, ContactRelationship data);

    void updateRelation(Long customerId, Long contactId, Long relationId, ContactRelationship data);

    void deleteRelation(Long customerId, Long contactId, Long relationId);

    List<ContactRelationship> queryRelation(Long customerId, Long contactId, ContactRelationshipFilter filter, PaginationDataDualLongString pagination);

    ContactRelationship getRelation(Long customerId, Long contactId, Long relationId);

        
}
