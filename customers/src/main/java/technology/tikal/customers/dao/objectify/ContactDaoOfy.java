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
import technology.tikal.customers.dao.filter.ContactFilter;
import technology.tikal.customers.dao.filter.NamePriorityFilterValues;
import technology.tikal.customers.dao.objectify.paginator.ContactNamePaginator;
import technology.tikal.customers.dao.objectify.paginator.ContactRolePaginator;
import technology.tikal.customers.dao.objectify.paginator.PaginationContext;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.contact.ContactInfoOfy;
import technology.tikal.customers.model.contact.ContactOfy;
import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.pagination.model.PaginationDataDual;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author Nekorp
 * los contactos no tienen roles, pero se busca por rol para inferir el tipo associate, asi no se tienen que indexar los discriminadores de tipo
 *
 */
public class ContactDaoOfy implements ContactDao {

    private ContactNamePaginator contactNamePaginator;
    private ContactRolePaginator contactRolePaginator;
    
    public ContactDaoOfy() {
        contactNamePaginator = new ContactNamePaginator();
        contactRolePaginator = new ContactRolePaginator();
    }
    
    @Override
    public List<ContactOfy> consultarTodos(CustomerOfy parent, ContactFilter filtro, PaginationDataDual<Long, String> pagination) {
        if (filtro.getIndex().compareTo("Role") == 0) {
            String indexOfy = "normalizedRole";
            return contactRolePaginator.consultarTodosTemplate(new PaginationContext<CustomerOfy>(indexOfy, filtro.getIndex(), filtro, pagination, parent));
        } else {
            NamePriorityFilterValues namePriority;
            try {
                namePriority = Enum.valueOf(NamePriorityFilterValues.class, filtro.getIndex());
            } catch (IllegalArgumentException ex) {
                throw new MessageSourceResolvableException(new DefaultMessageSourceResolvable(
                        new String[]{"IllegalIndex.ContactDaoOfy.consultarTodos"}, 
                        new String[]{filtro.getIndex()}, 
                        "Invalid Index"));
            }
            String indexOfy = "normalized" + filtro.getIndex();
            if (namePriority != NamePriorityFilterValues.Name) {
                indexOfy = "name." + indexOfy;
            }
            return contactNamePaginator.consultarTodosTemplate(new PaginationContext<CustomerOfy>(indexOfy, filtro.getIndex(), filtro, pagination, parent));
        }
    }

    @Override
    public void guardar(CustomerOfy parent, ContactOfy objeto) {
        if(objeto.isDirty()) {
            objeto.updateInternalIndex();
            ofy().save().entity(objeto).now();
            objeto.clean();
        }
        if (objeto.hasLoadedContactInfo()) {
            saveTransientInfo(objeto);
        }
    }
    
    private void saveTransientInfo(ContactOfy objeto) {
        ContactInfoOfy contactInfo = objeto.getInfoContact();
        if (contactInfo == null) {
            return;
        }
        if (contactInfo.getId() == null) {
            // no se habia guardado antes se le pone el owner
            contactInfo.setOwner(objeto);
            ofy().save().entity(contactInfo).now();
            contactInfo.clean();
            
            //se le pone al owner la referencia al info
            objeto.setInfoReference(contactInfo);
            ofy().save().entity(objeto).now();
            objeto.clean();
        } else {
            if (contactInfo.isDirty()) {
                ofy().save().entity(contactInfo).now();
                contactInfo.clean();
            }
        }
    }

    
    @Override
    public ContactOfy consultar(CustomerOfy parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(Key.create(parent), ContactOfy.class, id)).safe();
    }

    @Override
    public void borrar(CustomerOfy parent, ContactOfy objeto) {
        ofy().delete().entities(objeto).now();
    }    
}
