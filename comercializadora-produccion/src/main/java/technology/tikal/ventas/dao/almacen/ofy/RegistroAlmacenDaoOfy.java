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
package technology.tikal.ventas.dao.almacen.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenDao;
import technology.tikal.ventas.dao.almacen.RegistroAlmacenFilter;
import technology.tikal.ventas.model.almacen.ofy.EntradaOfy;
import technology.tikal.ventas.model.almacen.ofy.RegistroAlmacenOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * 
 * @author Nekorp
 *
 */
public abstract class RegistroAlmacenDaoOfy<T extends RegistroAlmacenOfy> implements RegistroAlmacenDao<T> {

    @Override
    public List<T> consultarTodos(Pedido parent, RegistroAlmacenFilter filtro, PaginationData<Long> pagination) {
        List<T> result;
        Query<T> query = buildQuery();
        query = query.ancestor(Key.create(parent));
        if (filtro != null && filtro.getProducto() != null) {
            query = query.filter("producto", Ref.create(filtro.getProducto()));
        }
        if (filtro != null && filtro.getIdProveedor() != null) {
            query = query.filter("idProveedor", filtro.getIdProveedor());
        }
        if (filtro != null && filtro.getFechaInicio() != null && filtro.getFechaFinal() != null) {
            query = query.filter("fechaEntrega >=", filtro.getFechaInicio());
            query = query.filter("fechaEntrega <=", filtro.getFechaFinal());
        }
        if (filtro != null && filtro.getOrigen() != null) {
            query = query.filter("origen", Ref.create(filtro.getOrigen()));
        }
        if (filtro != null && filtro.getReferenciaEnvio() != null) {
            query = query.filter("referenciaEnvio", filtro.getReferenciaEnvio());
        }
        if (pagination.hasSinceId()) {
            query = query.filterKey(">=", Key.create(EntradaOfy.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            T ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getId());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(Pedido parent, T objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public T consultar(Pedido parent, Long id, Class<?>... group) {
        return ofy().load().group(group).key(buildKey(parent, id)).safe();
    }

    @Override
    public void borrar(Pedido parent, T objeto) {
        ofy().delete().entities(objeto).now();
    }
    
    public abstract Query<T> buildQuery();
    
    public abstract Key<T> buildKey(Pedido parent, Long id);

}
