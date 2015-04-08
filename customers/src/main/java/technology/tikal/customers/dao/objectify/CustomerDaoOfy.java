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
package technology.tikal.customers.dao.objectify;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.googlecode.objectify.Key;

import technology.tikal.customers.dao.ContactDao;
import technology.tikal.customers.dao.CustomerDao;
import technology.tikal.customers.dao.filter.CustomerFilter;
import technology.tikal.customers.dao.filter.NamePriorityFilterValues;
import technology.tikal.customers.dao.objectify.paginator.CustomerNamePaginator;
import technology.tikal.customers.dao.objectify.paginator.PaginationContext;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.contact.PrimaryContact;
import technology.tikal.customers.model.contact.PrimaryContactOfy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataDual;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class CustomerDaoOfy implements CustomerDao {
    
    private ContactDao contactDao;
    private CustomerNamePaginator paginatorDelegate;
    
    public CustomerDaoOfy () {
        paginatorDelegate = new CustomerNamePaginator();
    }
    
    @Override
    public List<CustomerOfy> consultarTodos(CustomerFilter filtro, PaginationDataDual<Long, String> pagination) {
        NamePriorityFilterValues namePriority;
        try {
            namePriority = Enum.valueOf(NamePriorityFilterValues.class, filtro.getIndex());
        } catch (IllegalArgumentException ex) {
            throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                    new String[]{"IllegalIndex.CustomerDaoOfy.consultarTodos"}, 
                    new String[]{filtro.getIndex()}, 
                    "Invalid Index"));
        }
        String indexOfy = "normalized" + filtro.getIndex();
        if (namePriority != NamePriorityFilterValues.Name) {
            indexOfy = "name." + indexOfy;
        }
        return paginatorDelegate.consultarTodosTemplate(new PaginationContext<Object>(indexOfy, filtro.getIndex(), filtro, pagination));
    }

    @Override
    public void guardar(CustomerOfy objeto) {
        if (objeto.isDirty()) {
            objeto.updateInternalIndex();
            ofy().save().entity(objeto).now();
            objeto.clean();
        }
        if(objeto.hasLoadedContact()) {
            saveTransientInfo(objeto);
        }
    }
    
    private void saveTransientInfo(CustomerOfy objeto) {
        PrimaryContact rawContact = objeto.getPrimaryContact();
        if (rawContact == null) {
            return;
        }
        if (rawContact instanceof PrimaryContactOfy) {
            PrimaryContactOfy contact = (PrimaryContactOfy) objeto.getPrimaryContact();
            if (contact.getId() == null) {
                // no se habia guardado antes se le pone el owner
                contact.setOwner(objeto);
                contactDao.guardar(objeto, contact);
                
                //se le pone al owner la referencia al contacto
                objeto.setContactReference(contact);
                ofy().save().entity(objeto).now();
                objeto.clean();
            } else {
                contactDao.guardar(objeto, contact);
            }
        } else {
            throw new IllegalArgumentException("tipo no soportado");
        }
    }

    @Override
    public CustomerOfy consultar(Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(CustomerOfy.class, id)).safe();
    }

    @Override
    public void borrar(CustomerOfy objeto) {
        ofy().delete().entities(objeto).now();
    }

    public void setContactDao(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public void setPaginatorDelegate(CustomerNamePaginator paginatorDelegate) {
        this.paginatorDelegate = paginatorDelegate;
    }        
    
}
