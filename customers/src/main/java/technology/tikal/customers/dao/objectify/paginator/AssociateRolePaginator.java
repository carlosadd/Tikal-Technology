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
package technology.tikal.customers.dao.objectify.paginator;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.customers.dao.filter.ContactFilter;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.contact.AssociateOfy;

/**
 * @author Nekorp
 * se usa en conjunto con el DAO de associate para las busquedas, no se usa por el costo en indices
 *
 */
@Deprecated
public class AssociateRolePaginator extends StringPaginator<AssociateOfy, CustomerOfy> {   
    
    @Override
    public Query<AssociateOfy> buildQuery(PaginationContext<CustomerOfy> context) {
        Query<AssociateOfy> query = ofy().load().type(AssociateOfy.class);
        query = query.ancestor(Key.create(context.getParent()));
        return query;
    }

    @Override
    public Long getId(AssociateOfy pojo, PaginationContext<CustomerOfy> context) {
        return pojo.getId();
    }

    @Override
    public String getIndexValue(AssociateOfy pojo, PaginationContext<CustomerOfy> context) {
        return pojo.getRole();
    }

    @Override
    public String getStringFilter(PaginationContext<CustomerOfy> context) {
        ContactFilter filtro = (ContactFilter) context.getFilter();
        return filtro.getFilter();
    }    
}
