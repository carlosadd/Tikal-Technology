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
import technology.tikal.customers.model.contact.ContactOfy;
import technology.tikal.customers.model.name.IndexedByString;

/**
 * 
 * @author Nekorp
 *
 */
public class ContactNamePaginator extends StringPaginator<ContactOfy, CustomerOfy> {   
    
    @Override
    public Query<ContactOfy> buildQuery(PaginationContext<CustomerOfy> context) {
        Query<ContactOfy> query = ofy().load().type(ContactOfy.class);
        query = query.ancestor(Key.create(context.getParent()));
        query = query.order(context.getIndexOfy());
        return query;
    }

    @Override
    public Long getId(ContactOfy pojo, PaginationContext<CustomerOfy> context) {
        return pojo.getId();
    }

    @Override
    public String getIndexValue(ContactOfy pojo, PaginationContext<CustomerOfy> context) {
        if (pojo.getName() instanceof IndexedByString) {
            IndexedByString indexedName = (IndexedByString) pojo.getName();
            return indexedName.getIndex(context.getIndex());
        } else {
            return pojo.getName().toString();
        }
    }

    @Override
    public String getStringFilter(PaginationContext<CustomerOfy> context) {
        ContactFilter filtro = (ContactFilter) context.getFilter();
        return filtro.getFilter();
    }    
}
