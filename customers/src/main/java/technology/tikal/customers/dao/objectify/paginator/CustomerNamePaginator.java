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

import org.apache.commons.lang.StringUtils;

import com.googlecode.objectify.cmd.Query;

import technology.tikal.customers.dao.filter.CustomerFilter;
import technology.tikal.customers.model.CustomerOfy;
import technology.tikal.customers.model.name.IndexedByString;
import technology.tikal.string.util.StringNormalizer;

/**
 * 
 * @author Nekorp
 *
 */
public class CustomerNamePaginator extends StringPaginator<CustomerOfy, Object> {

    @Override
    public Query<CustomerOfy> buildQuery(PaginationContext<Object> context) {
        Query<CustomerOfy> query = ofy().load().type(CustomerOfy.class);
        if(context.getFilter() instanceof CustomerFilter) {
            CustomerFilter filtro = (CustomerFilter) context.getFilter();
            if (StringUtils.isNotEmpty(filtro.getGroup())) {
                query = query.filter("group.standarizedName", StringNormalizer.normalize(filtro.getGroup()));
            }
        }
        query = query.order(context.getIndexOfy());
        return query;
    }

    @Override
    public Long getId(CustomerOfy pojo, PaginationContext<Object> context) {
        return pojo.getId();
    }

    @Override
    public String getIndexValue(CustomerOfy pojo, PaginationContext<Object> context) {
        if (pojo.getName() instanceof IndexedByString) {
            IndexedByString indexedName = (IndexedByString) pojo.getName();
            return indexedName.getIndex(context.getIndex());
        } else {
            return pojo.getName().toString();
        }
    }

    @Override
    public String getStringFilter(PaginationContext<Object> context) {
        CustomerFilter filter = (CustomerFilter) context.getFilter();
        return filter.getFilter();
    }
}
