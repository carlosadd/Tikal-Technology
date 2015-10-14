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
package technology.tikal.ventas.dao.pedimento.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.ventas.dao.pedimento.PedimentoDao;
import technology.tikal.ventas.dao.pedimento.PedimentoFilter;
import technology.tikal.ventas.model.pedido.Pedido;
import technology.tikal.ventas.model.pedimento.ofy.PedimentoOfy;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class PedimentoDaoOfy implements PedimentoDao {

    @Override
    public List<PedimentoOfy> consultarTodos(Pedido parent, PedimentoFilter filtro, PaginationData<Long> pagination) {
        List<PedimentoOfy> result;
        Query<PedimentoOfy> query = ofy().load().type(PedimentoOfy.class);
        query = query.ancestor(Key.create(parent));
        if (filtro != null && filtro.getProducto() != null) {
            query = query.filter("producto", Ref.create(filtro.getProducto()));
        }
        if (filtro != null && filtro.getIdProveedor() != null) {
            query = query.filter("idProveedor", filtro.getIdProveedor());
        }
        //query = query.order("-fechaDeCreacion");
        if (pagination.hasSinceId()) {
            query = query.filterKey(">=", Key.create(PedimentoOfy.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            PedimentoOfy ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getId());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(Pedido parent, PedimentoOfy objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public PedimentoOfy consultar(Pedido parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(Key.create(Key.create(parent), PedimentoOfy.class, id)).safe();
    }

    @Override
    public void borrar(Pedido parent, PedimentoOfy objeto) {
        ofy().delete().entities(objeto).now();
    }

}
