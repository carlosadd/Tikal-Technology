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
import com.googlecode.objectify.Key;
import technology.tikal.customers.dao.AssociateDao;
import technology.tikal.customers.dao.filter.ContactFilter;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.contact.AssociateOfy;
import technology.tikal.gae.pagination.model.PaginationDataDual;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author Nekorp
 * No se usa por el costo en indices de busquedas por tipo
 *
 */
@Deprecated
public class AssociateDaoOfy implements AssociateDao {

    
    //se deshabilito por costo en indices
    //private AssociateNamePaginator contactNamePaginator;
    //private AssociateRolePaginator contactRolePaginator;
    private ContactDaoOfy delegate;
    
    public AssociateDaoOfy () {
        //se deshabilito por costo en indices
        //this.contactNamePaginator = new AssociateNamePaginator();
        //this.contactRolePaginator = new AssociateRolePaginator();
    }
    
    @Override
    public List<AssociateOfy> consultarTodos(CustomerOfy parent, ContactFilter filtro, PaginationDataDual<Long, String> pagination) {
        throw new UnsupportedOperationException("se deshabilito por el costo en indices");
        /*
        if (filtro.getPriority().compareTo("Role") == 0) {
            String indexOfy = "normalizedRole";
            return contactRolePaginator.consultarTodosTemplate(new PaginationContext<CustomerOfy>(indexOfy, filtro.getPriority(), filtro, pagination, parent));
        } else {
            NamePriorityFilterValues namePriority = Enum.valueOf(NamePriorityFilterValues.class, filtro.getPriority());
            String indexOfy = "normalized" + filtro.getPriority();
            if (namePriority != NamePriorityFilterValues.Name) {
                indexOfy = "name." + indexOfy;
            }
            return contactNamePaginator.consultarTodosTemplate(new PaginationContext<CustomerOfy>(indexOfy, filtro.getPriority(), filtro, pagination, parent));
        }
        */
    }
    @Override
    public void guardar(CustomerOfy parent, AssociateOfy objeto) {
        delegate.guardar(parent, objeto);
    }
    @Override
    public AssociateOfy consultar(CustomerOfy parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(Key.create(parent), AssociateOfy.class, id)).safe();
    }
    @Override
    public void borrar(CustomerOfy parent, AssociateOfy objeto) {
        delegate.borrar(parent, objeto);
    }
    public void setDelegate(ContactDaoOfy delegate) {
        this.delegate = delegate;
    }
}
