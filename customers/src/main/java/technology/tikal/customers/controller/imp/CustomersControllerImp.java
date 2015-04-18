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
package technology.tikal.customers.controller.imp;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.NotFoundException;

import technology.tikal.customers.controller.CustomersController;
import technology.tikal.customers.dao.ContactDao;
import technology.tikal.customers.dao.ContactRelationsDao;
import technology.tikal.customers.dao.CustomerDao;
import technology.tikal.customers.dao.filter.ContactFilter;
import technology.tikal.customers.dao.filter.ContactFilterSmall;
import technology.tikal.customers.dao.filter.ContactRelationshipFilter;
import technology.tikal.customers.dao.filter.CustomerFilter;
import technology.tikal.customers.dao.filter.CustomerFilterSmall;
import technology.tikal.customers.model.Customer;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.CustomerOfyFactory;
import technology.tikal.customers.model.CustomerOfy.CustomerOfyFull;
import technology.tikal.customers.model.contact.Contact;
import technology.tikal.customers.model.contact.ContactOfy;
import technology.tikal.customers.model.contact.ContactOfyFactory;
import technology.tikal.customers.model.contact.ContactRelationship;
import technology.tikal.customers.model.contact.ContactRelationshipOfy;
import technology.tikal.customers.model.contact.ContactOfy.ContactOfyFullInfo;
import technology.tikal.customers.model.contact.proxy.SmallContactRelationshipProxy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataDualLongString;

/**
 * 
 * @author Nekorp
 *
 */
public class CustomersControllerImp implements CustomersController {

    private CustomerDao customerDao;
    private ContactRelationsDao contactRelationsDao;
    private ContactDao contactDao;
    
    @Override
    public Customer createCustomer(Customer data) {
        CustomerOfy nuevo = CustomerOfyFactory.buildInternal(data);
        this.customerDao.guardar(nuevo);
        return nuevo;
    }
    
    @Override
    public Customer createCustomerWithForcedId(Long id, Customer data) {
        try {
            this.customerDao.consultar(id);
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"DuplicatedId.CustomersControllerImp.createCustomerWithForcedId"}, 
                    new String[]{id.toString()}, 
                    "Duplicated Id for new customer"));
        } catch (NotFoundException e) {
            CustomerOfy nuevo = CustomerOfyFactory.buildInternalWithForcedId(id, data);
            this.customerDao.guardar(nuevo);
            return nuevo;
        }
    }

    @Override
    public void updateCustomer(Long customerId, Customer data) {
        CustomerOfy customer = loadActiveCustomer(customerId, CustomerOfyFull.class);
        customer.update(data);
        this.customerDao.guardar(customer);
    }
    
    @Override
    public void deactivateCustomer(Long customerId) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        customer.setActive(false);
        this.customerDao.guardar(customer);
    }

    @Override
    public Customer[] queryCustomers(CustomerFilterSmall filter, PaginationDataDualLongString pagination) {
        List<CustomerOfy> result = this.customerDao.consultarTodos(new CustomerFilter(filter), pagination);
        Customer[] response = new Customer[result.size()];
        int index = 0;
        for(CustomerOfy x : result) {
            response[index] = x.buildProxy();
            index = index + 1;
        }
        return response;
    }

    @Override
    public Customer getCustomer(Long customerId) {
        CustomerOfy customer = loadActiveCustomer(customerId, CustomerOfyFull.class);
        return customer;
    }
    
    private CustomerOfy loadActiveCustomer(Long customerId, Class<?>... group) {
        try {
            CustomerOfy customer = this.customerDao.consultar(customerId, group);
            if (!customer.isActive()) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"CustomerNotActive.CustomersControllerImp.loadActiveCustomer"}, 
                        new String[]{customerId.toString()}, 
                        "Customer not active"));
            }
            return customer;
        } catch (NotFoundException ex) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotFound.CustomersControllerImp.loadActiveCustomer"}, 
                    new String[]{customerId.toString()}, 
                    "Customer not found"));
        }
    }
    
    @Override
    public Contact createContact(Long customerId, Contact data) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy nuevo = ContactOfyFactory.build(customer, data);
        contactDao.guardar(customer, nuevo);
        return nuevo;
    }

    @Override
    public void updateContact(Long customerId, Long contactId, Contact data) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contact = loadActiveContact(customer, contactId, ContactOfyFullInfo.class);
        contact.update(data);
        this.contactDao.guardar(customer, contact);
    }

    @Override
    public void deactivateContact(Long customerId, Long contactId) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contact = loadActiveContact(customer, contactId);
        contact.setActive(false);
        this.contactDao.guardar(customer, contact);
    }

    @Override
    public Contact[] queryContact(Long customerId, ContactFilterSmall filter, PaginationDataDualLongString pagination) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        List<ContactOfy> result = this.contactDao.consultarTodos(customer, new ContactFilter(filter), pagination);
        Contact[] response = new Contact[result.size()];
        int index = 0;
        for(ContactOfy x : result) {
            response[index] = x.buildProxy();
            index = index + 1;
        }
        return response;
    }

    @Override
    public Contact getContact(Long customerId, Long contactId) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contact = loadActiveContact(customer, contactId, ContactOfyFullInfo.class);
        return contact;
    }
    
    private ContactOfy loadActiveContact(CustomerOfy customer, Long contactId, Class<?>... group) {
        try {
            ContactOfy contact = this.contactDao.consultar(customer, contactId, group);
            if (!contact.isActive()) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"CustomerNotActive.CustomersControllerImp.loadActiveContact"}, 
                        new String[]{contactId.toString()}, 
                        "Contact not active"));
            }
            return contact;
        } catch (NotFoundException ex) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"NotFound.CustomersControllerImp.loadActiveContact"}, 
                    new String[]{contactId.toString()}, 
                    "Contact not found"));
        }
    }

    @Override
    public ContactRelationship createRelation(Long customerId, Long contactId, ContactRelationship data) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contactA = loadActiveContact(customer, contactId);
        ContactOfy contactB;
        if (data.getContact().getId() == null) { // no existe contactB se crea
            contactB = ContactOfyFactory.build(customer, data.getContact());
            this.contactDao.guardar(customer, contactB);
        } else {
            contactB = loadActiveContact(customer, data.getContact().getId());
        }
        validateRelation(contactA, contactB);
        ContactRelationshipOfy nueva = new ContactRelationshipOfy(contactA, data.getRelationship(), contactB);
        contactRelationsDao.guardar(contactA, nueva);
        return nueva;
    }

    @Override
    public void updateRelation(Long customerId, Long contactId, Long relationId, ContactRelationship data) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contactA = loadActiveContact(customer, contactId);
        ContactRelationshipOfy old = contactRelationsDao.consultar(contactA, relationId);
        ContactOfy contactB;
        if (data.getContact().getId() == null) { // no existe contactB se crea
            contactB = ContactOfyFactory.build(customer, data.getContact());
            this.contactDao.guardar(customer, contactB);
        } else {
            contactB = loadActiveContact(customer, data.getContact().getId());
        }
        validateRelation(contactA, contactB);
        old.setContact(contactB);
        old.setRelationship(data.getRelationship());
        contactRelationsDao.guardar(contactA, old);
    }
    
    private void validateRelation(ContactOfy contactA, ContactOfy contactB) {
        if (contactA.getId().equals(contactB.getId())) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"SameId.CustomersControllerImp.validateRelation"}, 
                    new String[]{contactB.getId().toString()}, 
                    "Invalid relation with himself"));
        }
    }
    
    @Override
    public void deleteRelation(Long customerId, Long contactId, Long relationId) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contactA = loadActiveContact(customer, contactId);
        ContactRelationshipOfy old = contactRelationsDao.consultar(contactA, relationId);
        contactRelationsDao.borrar(contactA, old);
    }
    
    @Override
    public List<ContactRelationship> queryRelation(Long customerId, Long contactId, ContactRelationshipFilter filter, PaginationDataDualLongString pagination) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contactA = loadActiveContact(customer, contactId);
        List<ContactRelationshipOfy> result = contactRelationsDao.consultarTodos(contactA, filter, pagination);
        List<ContactRelationship> response = new LinkedList<>();
        for (ContactRelationshipOfy x: result) {
            response.add(new SmallContactRelationshipProxy(x));
        }
        return response;
    }
    
    @Override
    public ContactRelationship getRelation(Long customerId, Long contactId, Long relationId) {
        CustomerOfy customer = loadActiveCustomer(customerId);
        ContactOfy contactA = loadActiveContact(customer, contactId);
        return contactRelationsDao.consultar(contactA, relationId, ContactOfyFullInfo.class);
    }
    
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void setContactRelationsDao(ContactRelationsDao contactRelationsDao) {
        this.contactRelationsDao = contactRelationsDao;
    }

    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }    
}
