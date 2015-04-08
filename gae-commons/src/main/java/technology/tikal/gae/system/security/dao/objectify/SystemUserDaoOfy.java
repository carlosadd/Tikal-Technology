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
package technology.tikal.gae.system.security.dao.objectify;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.gae.system.security.dao.SystemUserDao;
import technology.tikal.gae.system.security.model.SystemUser;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class SystemUserDaoOfy implements SystemUserDao {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (!StringUtils.isEmpty(username)) {
                return consultar(username);
            } else {
                throw new UsernameNotFoundException("UserNotFound");
            }
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("UserNotFound");
        }
    }

    @Override
    public List<SystemUser> consultarTodos(final FiltroBusqueda filtro, final PaginationData<String> pagination) {
        List<SystemUser> result;
        Query<SystemUser> query = ofy().load().type(SystemUser.class);
        if (StringUtils.isNotEmpty(pagination.getSinceId())) {
            query = query.filterKey(">=", Key.create(SystemUser.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            SystemUser ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getUsername());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(SystemUser objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public SystemUser consultar(String id, Class<?>... group) {
        return ofy().load().key(Key.create(SystemUser.class, id)).safe();
    }

    @Override
    public void borrar(SystemUser objeto) {
        ofy().delete().entities(objeto).now();
    }
}
