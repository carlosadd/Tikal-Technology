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

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.List;
import com.googlecode.objectify.Key;
import technology.tikal.customers.dao.ContactRelationsDao;
import technology.tikal.customers.dao.filter.ContactRelationshipFilter;
import technology.tikal.customers.dao.objectify.paginator.ContactRelationPaginator;
import technology.tikal.customers.dao.objectify.paginator.PaginationContext;
import technology.tikal.customers.model.contact.ContactOfy;
import technology.tikal.customers.model.contact.ContactRelationshipOfy;
import technology.tikal.gae.pagination.model.PaginationDataDual;
 /**
  * 
  * @author Nekorp
  *
  */
public class ContactRelationsDaoOfy implements ContactRelationsDao {

    private ContactRelationPaginator paginatorDelegate;
    public ContactRelationsDaoOfy() {
        this.paginatorDelegate = new ContactRelationPaginator();
    }
    
    @Override
    public List<ContactRelationshipOfy> consultarTodos(ContactOfy parent, ContactRelationshipFilter filtro, PaginationDataDual<Long, String> pagination) {
        String indexOfy = "normalizedRelationship";
        return paginatorDelegate.consultarTodosTemplate(new PaginationContext<ContactOfy>(indexOfy, "", filtro, pagination, parent));
    }

    @Override
    public void guardar(ContactOfy parent, ContactRelationshipOfy objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public ContactRelationshipOfy consultar(ContactOfy parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(
                Key.create(parent), 
                ContactRelationshipOfy.class, 
                id
            )).safe();
    }

    @Override
    public void borrar(ContactOfy parent, ContactRelationshipOfy objeto) {
        ofy().delete().entities(objeto).now();
    }    
}
