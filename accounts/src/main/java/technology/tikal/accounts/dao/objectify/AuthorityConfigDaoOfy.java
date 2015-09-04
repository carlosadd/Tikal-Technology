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

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.accounts.dao.ConfigDao;
import technology.tikal.accounts.model.config.RoleAuthorityMapEntry;
import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationData;
/**
 * 
 * @author Nekorp
 *
 */
public class AuthorityConfigDaoOfy implements ConfigDao<RoleAuthorityMapEntry> {

    @Override
    public List<RoleAuthorityMapEntry> consultarTodos(FiltroBusqueda filtro,
            PaginationData<String> pagination) {
        List<RoleAuthorityMapEntry> result;
        Query<RoleAuthorityMapEntry> query = ofy().load().type(RoleAuthorityMapEntry.class);
        if (StringUtils.isNotEmpty(pagination.getSinceId())) {
            query = query.filterKey(">=", Key.create(RoleAuthorityMapEntry.class, pagination.getSinceId()));
        }
        
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            RoleAuthorityMapEntry ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getRole());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(RoleAuthorityMapEntry objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public RoleAuthorityMapEntry consultar(String id, Class<?>... group) {
        return ofy().load().key(Key.create(RoleAuthorityMapEntry.class, id)).safe();
    }

    @Override
    public void borrar(RoleAuthorityMapEntry objeto) {
        ofy().delete().entities(objeto).now();
    }

}
