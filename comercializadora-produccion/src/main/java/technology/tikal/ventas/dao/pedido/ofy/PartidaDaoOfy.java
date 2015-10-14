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
package technology.tikal.ventas.dao.pedido.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.ventas.dao.pedido.PartidaDao;
import technology.tikal.ventas.dao.pedido.PartidaFilter;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedido.ofy.PartidaOfy;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class PartidaDaoOfy implements PartidaDao {

    @Override
    public List<PartidaOfy> consultarTodos(Pedido parent, PartidaFilter filtro, PaginationData<Long> pagination) {
        List<PartidaOfy> result;
        Query<PartidaOfy> query = ofy().load().type(PartidaOfy.class);
        query = query.ancestor(Key.create(parent));
        if (filtro != null && filtro.getProducto() != null) {
            query = query.filter("referenciaProducto", Ref.create(filtro.getProducto()));
        }
        //query = query.order("-fechaDeCreacion");
        if (pagination.hasSinceId()) {
            query = query.filterKey(">=", Key.create(PartidaOfy.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            PartidaOfy ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getId());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(Pedido parent, PartidaOfy objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public PartidaOfy consultar(Pedido parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(Key.create(parent), PartidaOfy.class, id)).safe();
    }

    @Override
    public void borrar(Pedido parent, PartidaOfy objeto) {
        ofy().delete().entities(objeto).now();
    }

}
