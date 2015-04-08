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

import com.googlecode.objectify.cmd.Query;

import technology.tikal.customers.dao.filter.ContactRelationshipFilter;
import technology.tikal.customers.model.contact.ContactOfy;
import technology.tikal.customers.model.contact.ContactRelationshipOfy;

/**
 * @author Nekorp
 * Se creo como opcion para lo costoso en indices de buscar por tipo,
 * se resuelven tipos dependiendo de las propiedades que contienen
 *
 */
public class ContactRelationPaginator extends StringPaginator<ContactRelationshipOfy, ContactOfy> {   
    
    @Override
    public Query<ContactRelationshipOfy> buildQuery(PaginationContext<ContactOfy> context) {
        Query<ContactRelationshipOfy> query = ofy().load().type(ContactRelationshipOfy.class);
        query = query.order(context.getIndexOfy());
        query = query.ancestor(context.getParent());
        return query;
    }

    @Override
    public Long getId(ContactRelationshipOfy pojo, PaginationContext<ContactOfy> context) {
        return pojo.getId();
    }

    @Override
    public String getIndexValue(ContactRelationshipOfy pojo, PaginationContext<ContactOfy> context) {
        return pojo.getNormalizedRelationship();
    }

    @Override
    public String getStringFilter(PaginationContext<ContactOfy> context) {
        ContactRelationshipFilter filtro = (ContactRelationshipFilter) context.getFilter();
        return filtro.getFilter();
    }    
}
