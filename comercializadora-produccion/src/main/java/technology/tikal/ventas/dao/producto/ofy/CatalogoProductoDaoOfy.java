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
package technology.tikal.ventas.dao.producto.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.ventas.dao.producto.CatalogoProductoDao;
import technology.tikal.ventas.model.producto.ofy.CatalogoOfy;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class CatalogoProductoDaoOfy implements CatalogoProductoDao {

    @Override
    public List<CatalogoOfy> consultarTodos(FiltroBusqueda filtro, PaginationData<Long> pagination) {
        List<CatalogoOfy> result;
        Query<CatalogoOfy> query = ofy().load().type(CatalogoOfy.class);
        if (pagination.hasSinceId()) {
            query = query.filterKey(">=", Key.create(CatalogoOfy.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            CatalogoOfy ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getId());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(CatalogoOfy objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public CatalogoOfy consultar(Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(CatalogoOfy.class, id)).safe();
    }

    @Override
    public void borrar(CatalogoOfy objeto) {
        ofy().delete().entities(objeto).now();
    }
}
