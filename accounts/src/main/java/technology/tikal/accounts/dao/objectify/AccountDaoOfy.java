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
package technology.tikal.accounts.dao.objectify;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.accounts.dao.AccountDao;
import technology.tikal.accounts.dao.filter.AccountFilter;
import technology.tikal.accounts.model.InternalAccount;
import technology.tikal.gae.pagination.model.PaginationData;
import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * 
 * @author Nekorp
 *
 */
public class AccountDaoOfy implements AccountDao {

    @Override
    public List<InternalAccount> consultarTodos(AccountFilter filtro, PaginationData<String> pagination) {
        List<InternalAccount> result;
        Query<InternalAccount> query = ofy().load().type(InternalAccount.class);
        if (StringUtils.isNotEmpty(filtro.getUserFilter())){
            Key<InternalAccount> filterId = Key.create(InternalAccount.class, filtro.getUserFilter().toLowerCase());
            Key<InternalAccount> filterIdComodin = Key.create(InternalAccount.class, filtro.getUserFilter().toLowerCase() + "\uFFFD");
            if (StringUtils.isEmpty(pagination.getSinceId())) {
                query = query.filterKey(">=", filterId)
                        .filterKey("< ", filterIdComodin);
            } else {
                query = query.filterKey(">=", Key.create(InternalAccount.class, pagination.getSinceId().toLowerCase()))
                        .filterKey("< ", filterIdComodin);
            }
        } else {
            if (StringUtils.isNotEmpty(pagination.getSinceId())) {
                query = query.filterKey(">=", Key.create(InternalAccount.class, pagination.getSinceId().toLowerCase()));
            }
        }
        
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            InternalAccount ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getIdUser());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(InternalAccount objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public InternalAccount consultar(String id, Class<?>... group) {
        return ofy().load().key(Key.create(InternalAccount.class, id.toLowerCase())).safe();
    }

    @Override
    public void borrar(InternalAccount objeto) {
        ofy().delete().entities(objeto).now();
    }

}
