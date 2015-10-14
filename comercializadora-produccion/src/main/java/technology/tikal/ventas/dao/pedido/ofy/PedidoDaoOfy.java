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

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.ventas.dao.pedido.PedidoDao;
import technology.tikal.ventas.dao.pedido.PedidoFilter;
import technology.tikal.ventas.model.pedido.ofy.PedidoOfy;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public class PedidoDaoOfy implements PedidoDao {

    @Override
    public List<PedidoOfy> consultarTodos(PedidoFilter filtro, PaginationData<Long> pagination) {
        List<? extends PedidoOfy> result;
        Query<? extends PedidoOfy> query = null;
        if (filtro.getType() != null) {
            query = ofy().load().type(filtro.getType());
        } else {
            query = ofy().load().type(PedidoOfy.class);
        }
        if (filtro.getPedidoRaiz() != null) {
            query = query.filter("owner", Ref.create(filtro.getPedidoRaiz()));
        }
        //query = query.order("-fechaDeCreacion");
        if (pagination.hasSinceId()) {
            query = query.filterKey(">=", Key.create(filtro.getType(), pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            PedidoOfy ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getId());
            result.remove(pagination.getMaxResults());
        }
        List<PedidoOfy> response = new ArrayList<PedidoOfy>();
        response.addAll(result);
        return response;
    }

    @Override
    public void guardar(PedidoOfy objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public PedidoOfy consultar(Long id, Class<?>... group) {
        return ofy().load().key(Key.create(PedidoOfy.class, id)).safe();
    }

    @Override
    public void borrar(PedidoOfy objeto) {
        ofy().delete().entities(objeto).now();
    }

}
